import {AfterViewInit, Component, Input, OnChanges, OnInit, Output, ViewChild, EventEmitter} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import ImageUploader from 'quill-image-uploader';
import BlotFormatter, {AlignAction, DeleteAction, ImageSpec, ResizeAction} from 'quill-blot-formatter';
import {SystemEnvironment} from '../../../models/system-environment';
import {randomString} from '../../../utilities/text-utils';
import {environment} from '../../../../environments/environment';
import CustomImage from './custom-image';
import {getImageDimensions, scaleImageToSize} from '../../../utilities/image-utils';

require('aws-sdk/dist/aws-sdk');

const { htmlToText } = require('html-to-text');


Quill.register('modules/imageUploader', ImageUploader);
Quill.register('modules/blotFormatter', BlotFormatter);
Quill.register({
  'formats/image': CustomImage
});

class CustomImageSpec extends ImageSpec {
  getActions(): Array<any> {
    return [AlignAction, DeleteAction, ResizeAction];
  }
}


@Component({
  selector: 'app-input-rich-text',
  templateUrl: './input-rich-text.component.html',
  styleUrls: ['./input-rich-text.component.css', '../../../styles/animations.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: InputRichTextComponent,
      multi: true,
    },
  ],
})
export class InputRichTextComponent implements ControlValueAccessor, OnChanges, OnInit, AfterViewInit {


  // File format configurations
  static readonly supportedImageTypes: ReadonlyArray<string> = ['png', 'jpg', 'jpeg', 'gif'];
  static readonly maxImageSize = 5000000;
  static readonly maxImageDimension = 800;
  static readonly scaledImageJpegQuality = 75;

  static htmlToTextLibraryOptions = {
    tags: {
      img: { format: 'skip' },
      h1: { format: 'skip' },
      h2: { format: 'skip' },
    },
  };


  @ViewChild('editorElem', {static: true, read: QuillEditorComponent}) editorElementRef: QuillEditorComponent;

  /**
   * The name of the directory on the server to store media.  This should be unique like manufacturer.companyName
   */
  @Input() dirName: string;
  @Output() filesUploading = new EventEmitter<Set<string>>();

  content = '';

  showBlankTab = true;

  selectedTabIndex = 0;

  currentImageUploads = new Set<string>();

  isDisabled = false;
  readonly DISABLED_OPACITY = 0.65;
  disabledStyle = {opacity: 1.0};


  quillConfig = {
    toolbar: [
      ['bold', 'italic', 'underline'],
      [{ color: [] }],
      [{ align: [false, 'center', 'right'] }],
      [{ size: ['small', false, 'large', 'huge'] }],
      // ToDo: add custom fonts to quill like Courier New
      // [{ font: [] }],
      [{ header: 1 }],   // This may be confusing to users
      [{ script: 'super' }],
      ['link', 'image'],
    ],
    imageUploader: {
      upload: uploadImage
    },
    blotFormatter: {
      modules: ['Resize', 'DisplaySize', 'Toolbar'],
      specs: [
        CustomImageSpec,
      ],
      overlay: {
        style: {
          border: '2px solid red',
        }
      }
    },
  };

  quillStyles = {
    'min-height': '250px',
    backgroundColor: '#ffff'
  };

  /** Used to convert html from Quill to plain text */
  static htmlToText(html: string): string {
    return htmlToText(html, this.htmlToTextLibraryOptions);
  }


  ngOnInit(): void {
    // Set an attribute on the function uploadImage so it can be modified after passing the function to the imageUploader
    (uploadImage as any).dirName = this.dirName;
    (uploadImage as any).currentImageUploads = this.currentImageUploads;
    (uploadImage as any).filesUploading = this.filesUploading;
  }


  ngOnChanges(changes): void {
    // dirName isn't initialized because its value is from an async function
    this.dirName = changes.dirName.currentValue;
    this.ngOnInit();
  }

  /**
   * This function is used as a hacky work around to overcome a bug with angular where the <mat-tab>
   * with index 0 isn't always selected on component render even if it is selected with selectedIndex prop.
   * This workaround adds a blank 3rd tab, waits 800ms after the component renders, then deletes that tab.
   */
  async ngAfterViewInit(): Promise<void> {
    setTimeout(() => {
      this.showBlankTab = false;
    }, 800);
  }






  // ------ControlValueAccessor implementations------

  writeValue(obj: any): void {
    this.content = obj;
    this.editorElementRef.writeValue(obj);
  }

  registerOnChange(fn: any): void {
    this.editorElementRef.registerOnChange(fn);
  }

  registerOnTouched(fn: any): void {
    this.editorElementRef.registerOnTouched(fn);
  }

  setDisabledState(isDisabled: boolean): void {
    this.editorElementRef.setDisabledState(isDisabled);

    this.isDisabled = isDisabled;
    if (isDisabled) {
      this.selectedTabIndex = 1;
      this.disabledStyle.opacity = this.DISABLED_OPACITY;
    } else {
      this.selectedTabIndex = 0;
      this.disabledStyle.opacity = 1.0;
    }
  }

}



function uploadImage(file: File): Promise<string> {

  // Keep track of ongoing uploads
  const uploadHash = randomString(10);
  this.upload.currentImageUploads.add(uploadHash);
  this.upload.filesUploading.emit(this.upload.currentImageUploads);

  let serverFilePath;
  if (this.upload && this.upload.dirName) {
    serverFilePath = '/manufacturer-editor/' + this.upload.dirName.toLowerCase();
  } else {
    serverFilePath = '/misc-editor-files';
  }


  return new Promise(  (resolve, reject) => {
    (async () => {
      let imageBlob: Blob;

      // Check file attributes
      if (!InputRichTextComponent.supportedImageTypes.map(s => 'image/' + s).includes(file.type)) {
        this.upload.currentImageUploads.delete(uploadHash);
        this.upload.filesUploading.emit(this.upload.currentImageUploads);
        reject('Unsupported file type ' + file.type + '. Files must be one of following: ' + InputRichTextComponent.supportedImageTypes);
        return;
      }

      const imageDimensions = await getImageDimensions(file);

      // Image too large?  Scale it.
      if (file.size > InputRichTextComponent.maxImageSize ||
        imageDimensions.width > InputRichTextComponent.maxImageDimension ||
        imageDimensions.height > InputRichTextComponent.maxImageDimension) {
        imageBlob = await scaleImageToSize(file,
          InputRichTextComponent.scaledImageJpegQuality, InputRichTextComponent.maxImageDimension);
      } else {
        imageBlob = file;
      }

      const uploadName = randomString(10) + '_' + file.name;

      const AWSService = (window as any).AWS;
      AWSService.config.accessKeyId = SystemEnvironment.AWS_ACCESS_KEY_ID;
      AWSService.config.secretAccessKey = SystemEnvironment.AWS_SECRET_ACCESS_KEY;
      const bucket = new AWSService.S3({params: {Bucket: environment.s3BucketName + serverFilePath}});
      const params = {Key: uploadName, Body: imageBlob};
      return bucket.upload(params, (error, response) => {

        this.upload.currentImageUploads.delete(uploadHash);
        this.upload.filesUploading.emit(this.upload.currentImageUploads);
        if (error) {
          reject('Error uploading to server: ' + error);
        } else {
          resolve(response.Location);
        }

      });
    })();
  });
}

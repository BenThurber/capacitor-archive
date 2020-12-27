import {AfterViewInit, Component, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import ImageUploader from 'quill-image-uploader';
import ImageResize from 'quill-image-resize-module';
import {Environment} from '../../models/environment';

Quill.register('modules/imageUploader', ImageUploader);
Quill.register('modules/imageResize', ImageResize);
require('aws-sdk/dist/aws-sdk');



@Component({
  selector: 'app-rich-text-input',
  templateUrl: './rich-text-input.component.html',
  styleUrls: ['./rich-text-input.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: RichTextInputComponent,
      multi: true,
    },
  ],
})
export class RichTextInputComponent implements ControlValueAccessor, OnChanges, OnInit, AfterViewInit {

  // File format configurations
  static readonly supportedImageTypes: ReadonlyArray<string> = ['png', 'jpg', 'jpeg', 'gif', 'jfif', 'webp'];
  static readonly maxImageSize = 5000000;


  @ViewChild('editorElem', {static: true, read: QuillEditorComponent}) editorElementRef: QuillEditorComponent;

  /**
   * The name of the directory on the server to store media.  This should be unique like manufacturer.companyName
   */
  @Input() dirName: string;

  content = '';


  showBlankTab = true;


  quillConfig = {
    toolbar: [
      ['bold', 'italic', 'underline'],
      [{ color: [] }],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ font: [] }],
      [{ header: 1 }],   // This may be confusing to users
      [{ align: [false, 'center', 'right'] }],
      ['link', 'image'],
    ],
    imageUploader: {
      upload: uploadImage
    },
    imageResize: {
      modules: ['Resize', 'DisplaySize', 'Toolbar']
    },
  };

  quillStyles = {
    height: '450px',
    backgroundColor: '#ffff'
  };


  ngOnInit(): void {
    // Set an attribute on the function uploadImage so it can be modified after passing the function to the imageUploader
    (uploadImage as any).dirName = this.dirName;
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



  editorChanged(event): void {
    if (event.event === 'text-change') {
      this.content = event.html;
    }
  }



  // ------ControlValueAccessor implementations------

  writeValue(obj: any): void {
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
  }

}


function randomString(length: number): string {
  let result           = '';
  const characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  const charactersLength = characters.length;
  for ( let i = 0; i < length; i++ ) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}


function uploadImage(file: File): Promise<string> {

  let serverFilePath;
  if (this.upload && this.upload.dirName) {
    serverFilePath = '/manufacturer-editor/' + this.upload.dirName;
  } else {
    serverFilePath = '/misc-editor-files';
  }


  return new Promise((resolve, reject) => {

    // Check file attributes
    if (!RichTextInputComponent.supportedImageTypes.map(s => 'image/' + s).includes(file.type)) {
      reject('Unsupported file type ' + file.type + '. Files must be one of following: ' + RichTextInputComponent.supportedImageTypes);
      return;
    }
    if (file.size > RichTextInputComponent.maxImageSize) {
      reject('File is too large.  Must be less than ' + Math.floor(RichTextInputComponent.maxImageSize / 1000000) + 'MB');
      return;
    }

    const uploadName = randomString(10) + '_' + file.name;

    const AWSService = (window as any).AWS;
    AWSService.config.accessKeyId = Environment.AWS_ACCESS_KEY_ID;
    AWSService.config.secretAccessKey = Environment.AWS_SECRET_ACCESS_KEY;
    const bucket = new AWSService.S3({params: {Bucket: 'capacitor-archive-media' + serverFilePath}});
    const params = {Key: uploadName, Body: file};
    return bucket.upload(params, (error, response) => {

      if (error) {
        reject('Error uploading to server: ' + error);
      } else {
        resolve(response.Location);
      }

    });
  });
}

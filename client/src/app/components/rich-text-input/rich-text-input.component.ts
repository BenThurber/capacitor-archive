import {Component, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import ImageUploader from 'quill-image-uploader';
import {Environment} from '../../models/environment';

Quill.register('modules/imageUploader', ImageUploader);
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
export class RichTextInputComponent implements ControlValueAccessor, OnChanges, OnInit {

  // File format configurations
  static readonly supportedImageTypes: ReadonlyArray<string> = ['png', 'jpg', 'jpeg', 'jfif', 'webp'];
  static readonly maxImageSize = 5000000;


  @ViewChild('editorElem', {static: true, read: QuillEditorComponent}) editorElementRef: QuillEditorComponent;

  /**
   * The name of the directory on the server to store media.  This should be unique like manufacturer.companyName
   */
  @Input() mediaDirectoryName: string;


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
  };

  quillStyles = {
    height: '450px',
    backgroundColor: '#ffff'
  };


  ngOnInit(): void {
    // Set an attribute on the function uploadImage so it can be modified after passing the function to the imageUploader
    (uploadImage as any).mediaDirectoryName = this.mediaDirectoryName;
  }


  ngOnChanges(changes): void {
    // mediaDirectoryName isn't initialized because its value is from an async function
    this.mediaDirectoryName = changes.mediaDirectoryName.currentValue;
    this.ngOnInit();
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


function uploadImage(file: File): Promise<string> {

  let serverFilePath;
  if (this.upload && this.upload.mediaDirectoryName) {
    serverFilePath = '/editor/' + this.upload.mediaDirectoryName;
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


    const AWSService = (window as any).AWS;
    AWSService.config.accessKeyId = Environment.AWS_ACCESS_KEY_ID;
    AWSService.config.secretAccessKey = Environment.AWS_SECRET_ACCESS_KEY;
    const bucket = new AWSService.S3({params: {Bucket: 'capacitor-archive-media' + serverFilePath}});
    const params = {Key: file.name, Body: file};
    return bucket.upload(params, (error, response) => {

      if (error) {
        reject('Error uploading to server: ' + error);
      } else {
        resolve(response.Location);
      }

    });
  });
}

import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import ImageUploader from 'quill-image-uploader';

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
export class RichTextInputComponent implements ControlValueAccessor, OnInit {

  // File format configurations
  static readonly supportedImageTypes: ReadonlyArray<string> = ['png', 'jpg', 'jpeg', 'jfif', 'webp'];
  static readonly maxImageSize = 5000000;
  static readonly supportedVideoTypes: ReadonlyArray<string> = ['mp4', 'webm'];


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
      // [{ header: 1 }],   This may be confusing to users
      [{ align: [false, 'center', 'right'] }],

      ['link', 'image'],
    ],

    imageUploader: {
      upload: this.uploadImage
    },
  };

  quillStyles = {
    height: '250px',
    backgroundColor: '#ffff'
  };


  constructor() {}

  ngOnInit(): void {}




  uploadImage(file): any {  // This shouldn't be any

    const AWSService = (window as any).AWS;

    let serverFilePath;
    if (this.mediaDirectoryName) {
      serverFilePath = encodeURI('/editor/' + this.mediaDirectoryName);
    } else {
      serverFilePath = encodeURI('/editor-files');
    }


    return new Promise((resolve, reject) => {

      // Check file attributes
      if (!RichTextInputComponent.supportedImageTypes.map(s => 'image/' + s).includes(file.type)) {
        reject('Unsupported file type ' + file.type);
        return;
      }
      if (file.size > RichTextInputComponent.maxImageSize) {
        reject('File is too large.  Must be less than ' + Math.floor(RichTextInputComponent.maxImageSize / 1000000) + 'MB');
        return;
      }

      AWSService.config.accessKeyId = '';
      AWSService.config.secretAccessKey = '';
      const bucket = new AWSService.S3({params: {Bucket: 'capacitor-archive-media' + serverFilePath}});
      const params = {Key: file.name, Body: file};
      return bucket.upload(params, (error, response) => {
        console.log('error:', error);
        console.log('response', response);
        if (error) {
          reject('Error uploading');
        } else {
          resolve(response.Location);
        }
      });

    });

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

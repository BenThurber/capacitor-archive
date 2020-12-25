import {Component, OnInit, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import {ImageHandler, Options, VideoHandler} from 'ngx-quill-upload';
import {HttpClient} from '@angular/common/http';

Quill.register('modules/imageHandler', ImageHandler);
Quill.register('modules/videoHandler', VideoHandler);

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

  static readonly supportedImageTypes: ReadonlyArray<string> = ['png', 'jpg', 'jpeg', 'jfif', 'webp'];
  static readonly maxImageSize = 5000000;
  static readonly supportedVideoTypes: ReadonlyArray<string> = ['mp4', 'webm'];


  @ViewChild('editorElem', {static: true, read: QuillEditorComponent}) editorElementRef: QuillEditorComponent;


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

    imageHandler: {
      upload: this.uploadImage,
      accepts: RichTextInputComponent.supportedImageTypes // Extensions to allow for images (Optional) | Default - ['jpg', 'jpeg', 'png']
    } as Options,
    videoHandler: {
      upload: (file) => {
        return new Promise((resolve, reject) => {}); // your uploaded video URL as Promise<string>
      },
      accepts: RichTextInputComponent.supportedVideoTypes  // Extensions to allow for videos (Optional) | Default - ['mp4', 'webm']
    } as Options,
  };

  quillStyles = {
    height: '250px',
    backgroundColor: '#ffff'
  };


  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {}




  uploadImage(file): any {  // This shouldn't be any
    console.log(file);
    return new Promise((resolve, reject) => {
      if (RichTextInputComponent.supportedImageTypes.map(s => 'image/' + s).includes(file.type)) { // File types supported for image
        if (file.size < RichTextInputComponent.maxImageSize) { // Customize file size as per requirement

          // Sample API Call
          const uploadData = new FormData();
          uploadData.append('file', file, file.name);

          const config = { headers: {'Content-Type': undefined}};

          return this.httpClient.put('https://s3-ap-southeast-2.amazonaws.com/capacitor-archive-media/myFile', uploadData, config).toPromise()
            .then(result => {
              console.log('result is:', result);
              resolve(result.message.url); // RETURN IMAGE URL from response
            })
            .catch(error => {
              reject('Upload failed');
              // Handle error control
            });
        } else {
          reject('Size too large');
          // Handle Image size large logic
        }
      } else {
        reject('Unsupported type');
        // Handle Unsupported type logic
      }
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

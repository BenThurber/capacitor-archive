import {Component, OnInit, ViewChild} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';
import Quill from 'quill';
import {ImageHandler, Options, VideoHandler} from 'ngx-quill-upload';

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
      upload: (file) => {
        return new Promise((resolve, reject) => {}); // your uploaded image URL as Promise<string>
      },
      accepts: ['png', 'jpg', 'jpeg', 'jfif', 'webp'] // Extensions to allow for images (Optional) | Default - ['jpg', 'jpeg', 'png']
    } as Options,
    videoHandler: {
      upload: (file) => {
        return new Promise((resolve, reject) => {}); // your uploaded video URL as Promise<string>
      },
      accepts: ['mp4', 'webm']  // Extensions to allow for videos (Optional) | Default - ['mp4', 'webm']
    } as Options,
  };

  quillStyles = {
    height: '250px',
    backgroundColor: '#ffff'
  };


  ngOnInit(): void {}







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

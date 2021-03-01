import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {Thumbnail} from '../../models/file/thumbnail.model';
import {FinishUploadEvent} from '../../models/finish-upload-event.model';
import {StartUploadEvent} from '../../models/start-upload-event.model';

require('src/app/utilities/canvas-plus.js');
const canvas = new (window as any).CanvasPlus();


@Component({
  selector: 'app-input-photo',
  templateUrl: './input-photo.component.html',
  styleUrls: ['./input-photo.component.css'],
})
export class InputPhotoComponent implements OnInit, ControlValueAccessor {

  @Input() dirPathArray: Array<string>;

  // photos: Record<string, Photo> = {};
  photos: Map<string, Photo> = new Map<string, Photo>();
  thumbnails: Record<string, Thumbnail> = {};

  options: any = {
    swapThreshold: 1.0,
    invertSwap: true,
    animation: 200,
    ghostClass: 'ghost',
    direction: 'horizontal',
    group: {
      name: 'shared',
    }
  };


  onChange = event => {};
  onTouched = () => {};


  constructor() { }

  ngOnInit(): void {
    console.log('canvas:', canvas);

    canvas.load( '../../../assets/paper-labels.webp', (err1) => {
      if (err1) { throw err1; }

      canvas.resize({
        width: 256,
        mode: 'fit',
      });

      canvas.write({format: 'jpeg', quality: 35}, (err2, buf) => {
        if (err2) { throw err2; }

        // 'buf' will be a binary buffer containing final image...
        const blob = new Blob( [ buf ], { type: 'image/jpeg' } );
        const objectUrl = URL.createObjectURL( blob );

        // insert new image into DOM
        const img = new Image();
        img.src = objectUrl;
        img.width = 150;
        console.log(img);
        console.log(blob);
        document.body.appendChild( img );
      });

    });
  }

  addPhoto(uploadedFile: FinishUploadEvent): void {
    const photo = new Photo(uploadedFile.url, null);

    const thumbnail = this.thumbnails[uploadedFile.serverPath];
    if (thumbnail) {
      photo.thumbnails.push(thumbnail);
    }

    this.photos.set(uploadedFile.serverPath, photo);
  }

  async generateThumbnail(uploadingFile: StartUploadEvent): Promise<void> {

    canvas.load( uploadingFile.file, (err1) => {
      if (err1) { throw err1; }

      canvas.resize({
        width: 256,
        mode: 'fit',
      });

      canvas.write({format: 'jpeg', quality: 35}, (err2, buf) => {
        if (err2) { throw err2; }

        // 'buf' will be a binary buffer containing final image...
        const blob = new Blob( [ buf ], { type: 'image/jpeg' } );
        const objectUrl = URL.createObjectURL( blob );

        // insert new image into DOM
        const img = new Image();
        img.src = objectUrl;
        img.height = 130;

        document.body.appendChild( img );
      });

    });

  }

  uploadThumbnail(thumbnail: Blob): void {
  }



  // ------ControlValueAccessor implementations------

  writeValue(photos: Array<Photo>): void {
    this.onChange(photos);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

}

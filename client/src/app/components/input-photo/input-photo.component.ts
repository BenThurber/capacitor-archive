import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {Thumbnail} from '../../models/file/thumbnail.model';
import {FinishUploadEvent} from '../../models/finish-upload-event.model';
import {StartUploadEvent} from '../../models/start-upload-event.model';


@Component({
  selector: 'app-input-photo',
  templateUrl: './input-photo.component.html',
  styleUrls: ['./input-photo.component.css'],
})
export class InputPhotoComponent implements OnInit, ControlValueAccessor {

  @Input() dirPathArray: Array<string>;

  photos: Record<string, Photo> = {};
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
  }

  addPhoto(uploadedFile: FinishUploadEvent): void {
    const photo = new Photo(uploadedFile.url, null);

    const thumbnail = this.thumbnails[uploadedFile.serverPath];
    if (thumbnail) {
      photo.thumbnails.push(thumbnail);
    }

    this.photos[uploadedFile.serverPath] = photo;
  }

  async generateThumbnail(uploadingFile: StartUploadEvent): Promise<void> {

    const arrayBuffer: ArrayBuffer = await uploadingFile.file.arrayBuffer();
    const buffer: Buffer = Buffer.from(arrayBuffer);  // Inefficient?


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

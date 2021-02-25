import { Component, OnInit } from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {SystemEnvironment} from '../../models/system-environment';
import {randomString} from '../../utilities/text-utils';

require('aws-sdk/dist/aws-sdk');
const AWS = (window as any).AWS;


@Component({
  selector: 'app-photo-upload',
  templateUrl: './photo-upload.component.html',
  styleUrls: ['./ngx-upload.sass', './photo-upload.component.css'],
})
export class PhotoUploadComponent implements OnInit, ControlValueAccessor {

  items: Array<number>;

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
    this.items = [1, 2, 3, 4, 5, 6];

    AWS.config.update({accessKeyId: SystemEnvironment.AWS_ACCESS_KEY_ID,
      secretAccessKey: SystemEnvironment.AWS_SECRET_ACCESS_KEY,
      region: 'ap-southeast-2',
    });
  }


  uploadFiles(fileList: Array<File> | FileList): void {

    for (let i = 0; i < fileList.length; i++) {
      console.log(fileList[i]);
      this.uploadSingleFile(fileList[i]);
    }
  }


  uploadSingleFile(file: File): void {

    const filename = file.name.split('.')[0];
    const extension = file.name.split('.')[file.name.lastIndexOf('.')];

    const bucket = new AWS.S3({params: {Bucket: 'capacitor-archive-media'}});
    const params = {Key: filename + '_' + randomString(10) + extension, Body: file};

    return bucket.upload(params).on('httpUploadProgress', (evt) => {
      console.log('Progress:', evt.loaded, '/', evt.total);
    }).send((err, data) => {
      console.log(err, data);
    });

  }


  /**
   * Handle event when item is dragged to drop-container
   * @param event the DragEvent containing the file(s)
   */
  onDrop(event: DragEvent): void {
    event.preventDefault();

    const { dataTransfer } = event;

    if (dataTransfer.items) {
      const files = [];
      for (let i = 0; i < dataTransfer.items.length; i++) {
        // If dropped items aren't files, reject them
        if (dataTransfer.items[i].kind === 'file') {
          files.push(dataTransfer.items[i].getAsFile());
        }
      }
      dataTransfer.items.clear();
      this.uploadFiles(files);
    } else {
      const files = dataTransfer.files;
      dataTransfer.clearData();
      this.uploadFiles(Array.from(files));
    }
  }
  /**
   * Needed with onDrop to suppress default browser behavior
   * @param event the DragEvent containing the file(s)
   */
  onDragOver(event: DragEvent): void {
    event.stopPropagation();
    event.preventDefault();
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

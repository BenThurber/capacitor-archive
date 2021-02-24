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
  styleUrls: ['./photo-upload.component.css']
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


  fileChosen(event): void {
    const fileList: FileList = event.target.files;

    for (let i = 0; i < fileList.length; i++) {
      console.log(fileList[i]);
      this.startFileUpload(fileList[i]);
    }
  }


  startFileUpload(file: File): void {

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

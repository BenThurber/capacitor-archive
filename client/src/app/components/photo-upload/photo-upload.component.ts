import { Component, OnInit } from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {SystemEnvironment} from '../../models/system-environment';
import {randomString} from '../../utilities/text-utils';

require('aws-sdk/dist/aws-sdk');
const AWS = (window as any).AWS;



class FileUpload extends File {
  progress: UploadProgress;
}

interface UploadProgress {
  data?: {
    percentage: number;
    speed?: number;
    speedHuman?: string;
    startTime?: number | null;
    endTime?: number | null;
    eta?: number | null;
    etaHuman?: string | null;
  };
}

@Component({
  selector: 'app-photo-upload',
  templateUrl: './photo-upload.component.html',
  styleUrls: ['./ngx-upload.sass', './photo-upload.component.css'],
})
export class PhotoUploadComponent implements OnInit, ControlValueAccessor {

  items: Array<number>;

  files: Array<FileUpload> = [new FileUpload([null], ''), new FileUpload([null], '')];

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


  addFilesToQueue(fileList: Array<File> | FileList): void {

    let file: File;
    let fileUpload: FileUpload;
    for (let i = 0; i < fileList.length; i++) {
      file = fileList[i];

      fileUpload = new FileUpload([file], file.name);
      this.files.push(fileUpload);
    }

    this.uploadAllFiles();
  }


  uploadAllFiles(): void {
    if (this.files.length === 0) {
      return;
    }

    const file = this.files[0];  // Get the next file in the queue

    const splitFilename = file.name.split('.');
    const filename = splitFilename[0];
    const extension = '.' + splitFilename[splitFilename.length - 1];

    const bucket = new AWS.S3({params: {Bucket: 'capacitor-archive-media/temp'}});
    const params = {Key: filename + '_' + randomString(10) + extension, Body: file};

    return bucket.upload(params).on('httpUploadProgress', (evt) => {
      console.log('Progress:', evt.loaded, '/', evt.total);
      // Calculate File Progress
      file.progress = {data: {percentage: Math.round((evt.loaded / evt.total) * 100)}};  // Use 100.001 to avoid floating point error

    }).send((err, data) => {
      console.log(err, data);
      this.files.shift();

      if (this.files.length > 0) {
        this.uploadAllFiles();
      }
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
      this.addFilesToQueue(files);
    } else {
      const files = dataTransfer.files;
      dataTransfer.clearData();
      this.addFilesToQueue(Array.from(files));
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

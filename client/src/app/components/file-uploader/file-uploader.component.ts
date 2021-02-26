import {Component, Input, OnInit} from '@angular/core';
import {SystemEnvironment} from '../../models/system-environment';
import {randomString} from '../../utilities/text-utils';
import {environment} from '../../../environments/environment';

require('aws-sdk/dist/aws-sdk');
const AWS = (window as any).AWS;


class FileUpload extends File {
  progress: UploadProgress = {data: {percentage: 0}};
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
  selector: 'app-file-uploader',
  templateUrl: './file-uploader.component.html',
  styleUrls: ['./ngx-upload.sass']
})
export class FileUploaderComponent implements OnInit {

  @Input() dirPathArray: Array<string>;

  files: Array<FileUpload> = [];
  currentUpload: any = null;
  bucket: any;

  constructor() { }


  ngOnInit(): void {
    AWS.config.update({accessKeyId: SystemEnvironment.AWS_ACCESS_KEY_ID,
      secretAccessKey: SystemEnvironment.AWS_SECRET_ACCESS_KEY,
      region: 'ap-southeast-2',
    });

    this.bucket = new AWS.S3({params: {}});
  }


  /**
   * Add files to the this.files queue (Array).  If no upload is currently in progress, begin uploading be calling uploadAllFiles().
   * @param fileList either an Array<File> or FileList.  An index-able container containing File objects.
   */
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


  /**
   * Recursively uploads each file contained in this.files.  Starts from index 0.  After the file is finished uploading,
   * it is removed from the queue (Array).
   *
   * Each file is uploaded to the S3 Bucket at the path given by dirPathArray.  The @Input directive dirPathArray
   * is an Array<string> which is joined by '/' to get the path on the S3 server to upload to.  If dirPathArray is not
   * specified, files are uploaded to /misc-files and a console warning is issued.
   *
   * When a File is read from the queue, it is assigned to this.currentUpload.  As the download progress changes, the
   * property file.progress.data.percentage is updated.  When the file is finished uploading, this.currentUpload is set
   * to null.
   */
  uploadAllFiles(): void {
    if (this.files.length === 0 || this.currentUpload) {
      return;
    }

    const file = this.files[0];  // Get the next file in the queue

    const splitFilename = file.name.split('.');
    const filename = splitFilename[0];
    const extension = '.' + splitFilename[splitFilename.length - 1];

    let bucketDir;
    try {
      bucketDir = environment.s3BucketName + '/' + this.dirPathArray.filter(s => s).join('/');
    } catch (e) {
      console.warn('No path specified for uploading files');
      bucketDir = environment.s3BucketName + '/misc-files';
    }

    const params = {
      Bucket: bucketDir,
      Key: filename + '_' + randomString(10) + extension,
      Body: file,
    };

    // Prepare the payload
    this.currentUpload = this.bucket.upload(params).on('httpUploadProgress', (evt) => {

      // Calculate File Progress
      file.progress.data.percentage = Math.round((evt.loaded / evt.total) * 100);

    });

    // Send the payload
    this.currentUpload.send((err, data) => {

      if (err) {
        console.warn(err.message);
      }

      this.files.shift();
      this.currentUpload = null;

      // Recursive call
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

  /**
   * If a file is pending, it is removed from this.files.  If the file is currently uploading, an abort request is sent
   * to the AWS S3 server.
   * @param index the index of the file in this.files
   */
  cancelUpload(index: number): void {

    // Is the file currently uploading or pending?
    if (index === 0) {
      setTimeout(this.currentUpload.abort.bind(this.currentUpload), 0);
    } else {
      this.files.splice(index, 1);
    }
  }

}

import {Component, forwardRef, Input, OnInit} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {Photo} from '../../../models/file/photo.model';
import {Thumbnail} from '../../../models/file/thumbnail.model';
import {FinishedUploadEvent, StartedUploadEvent} from '../../../models/upload-event.model';
import {AwsUploadResponse} from '../../../models/aws-upload-response';
import {ModalService} from '../../modal';
import {ThumbnailProperty} from '../../../models/thumbnail-property';
import {FileUploaderComponent} from '../../file-uploader/file-uploader.component';

require('src/app/utilities/canvas-plus.js');
const canvas = new (window as any).CanvasPlus();

require('aws-sdk/dist/aws-sdk');
const AWS = (window as any).AWS;


@Component({
  selector: 'app-input-photo',
  templateUrl: './input-photo.component.html',
  styleUrls: ['./input-photo.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR, multi: true,
      useExisting: forwardRef(() => InputPhotoComponent),
    }
  ],
})
export class InputPhotoComponent implements OnInit, ControlValueAccessor {

  readonly THUMBNAILS_TO_CREATE: Array<ThumbnailProperty> = [{width: 256, quality: 45}, {width: 768, quality: 75}];

  @Input() dirPathArray: Array<string>;

  public photos: Array<Photo> = [];
  private thumbnails: Array<Thumbnail> = [];

  bucket: any;

  options: any = {
    swapThreshold: 1.0,
    invertSwap: true,
    animation: 200,
    ghostClass: 'ghost',
    direction: 'horizontal',
    group: {
      name: 'shared',
    },
    onEnd: () => {this.onChange(this.photos); this.onTouched(); },
  };

  decodeURIComponent = decodeURIComponent;

  onChange = (value: Array<Photo>) => {};
  onTouched = () => {};


  constructor(private modalService: ModalService) { }

  ngOnInit(): void {
    // AWS config always needs be be set before a new bucket is created
    AWS.config.update(FileUploaderComponent.AWS_CONFIG);
    this.bucket = new AWS.S3({params: {}});
  }


  addPhoto(uploadedFile: FinishedUploadEvent): void {
    const photo = Photo.fromUrl(uploadedFile.url, null);

    // Attach photo to thumbnails
    const thumbnails = this.thumbnails.filter(th => th.referencesPhoto(photo));
    thumbnails.forEach(th => {
      th.photo = photo;
      photo.thumbnails.push(th);
    });

    this.photos.push(photo);
    this.onTouched();
    this.onChange(this.photos);
  }


  /**
   * Generates a thumbnail, uploads it the AWS S3 server, and pushes it to this.thumbnails array
   * @param uploadingFile event from file-uploader
   * @param thumbnailProperties an Array describing the properties of thumbnails to create; quality and width
   */
  async addThumbnails(uploadingFile: StartedUploadEvent, thumbnailProperties: Array<ThumbnailProperty>): Promise<void> {
    try {

      for (const thumbnailProperty of thumbnailProperties) {
        const thumbnailBlob = await this.scaleImageToSize(uploadingFile.file, thumbnailProperty.quality, thumbnailProperty.width);
        // upload and link asynchronously
        this.uploadAndLinkThumb(uploadingFile, thumbnailBlob, thumbnailProperty);
      }

    } catch (err) {
      console.error('Could not add thumbnail.', err.message && 'Message: ' + err.message);
      return;
    }

  }

  async uploadAndLinkThumb(uploadingFile: StartedUploadEvent, thumbnailBlob: Blob, thumbnailProperty: ThumbnailProperty): Promise<void> {
    const thumbnail = await this.uploadThumbnail(
      thumbnailBlob, uploadingFile.awsS3BucketDir, uploadingFile.filename, thumbnailProperty.width);


    // Attach thumbnail to photo
    const photo = this.photos.find(p => thumbnail.referencesPhoto(p));
    if (photo) {
      thumbnail.photo = photo;
      photo.thumbnails.push(thumbnail);
    }

    this.thumbnails.push(thumbnail);
    this.onTouched();
    this.onChange(this.photos);
  }


  /**
   * Convert a Blob image file to JPG.  Supports JPEG, PNG, GIF, BMP, WebP.
   * @param file in one of the formats JPEG, PNG, GIF, BMP, WebP
   * @param jpegQuality a number from 0 to 100
   * @param width the size of the final image
   */
  async scaleImageToSize(file: Blob, jpegQuality: number, width = 256): Promise<Blob> {
    await canvasLoadFile(file);

    canvas.resize({
      width,
      mode: 'fit',
      background: '#ffffff',
    });

    const buffer = await canvasWriteFile({format: 'jpeg', quality: jpegQuality});

    return new Blob( [ buffer ], { type: 'image/jpeg' } );
  }


  /**
   * Upload Blob to AWS S3 server.
   * @param blob Blob data of file
   * @param awsS3BucketDir  the directory in the bucket where to put the file (not including file name).
   * i.e. bucketName/folder1/folder2
   * @param filename what to name the file on the server
   * @param width of image (used as meta data)
   * @return a new Thumbnail object
   */
  async uploadThumbnail(blob: Blob, awsS3BucketDir: string, filename: string, width: number): Promise<Thumbnail> {

    const thumbnailFilename = Thumbnail.toThumbnailUrl(filename, width);

    const params = {
      Bucket: awsS3BucketDir,
      Key: thumbnailFilename,
      Body: blob,
    };

    const awsUploadResponse = await awsUploadFile(this.bucket, params);

    return Thumbnail.fromUrl(awsUploadResponse.Location, width);
  }


  deletePhoto(photo: Photo): void {
    const index = this.photos.findIndex(p => p === photo);
    this.photos.splice(index, 1);
    this.onTouched();
    this.onChange(this.photos);
  }



  // ------ControlValueAccessor implementations------

  writeValue(photos: Array<Photo>): void {
    if (photos) {
      this.photos = [...photos];
      this.onChange(this.photos);
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

}


/**
 * Wraps CanvasPlus load function to use async/await
 * @param file the File or Blob to load into the canvas
 */
function canvasLoadFile(file: Blob): Promise<void> {
  return new Promise((resolve, reject) => {
    canvas.load( file, (err) => {
      if (err) {reject(err); }
      resolve();
    });
  });
}

/**
 * Wraps CanvasPlus write function to use async/await
 * @param params an object containing the configuration of the file to be created.  i.e. {format: 'jpeg', quality: 50}
 */
function canvasWriteFile(params: object): Promise<Buffer> {
  return new Promise((resolve, reject) => {
    canvas.write(params, (err, buf) => {
      if (err) {reject(err); }
      resolve(buf);
    });
  });
}

/**
 * Wraps AWS-SDK upload function to use async/await
 * @param params an object containing the configuration of the file to be created.  {Bucket: ..., Key: ..., Body: ...}
 * @param bucket the bucket object to upload files to.  This must be configured and passed in.
 */
function awsUploadFile(bucket: any, params: object): Promise<AwsUploadResponse> {
  return new Promise((resolve, reject) => {
    bucket.upload(params).send((err, data) => {
      if (err) {reject(err); }
      resolve(data);
    });
  });
}

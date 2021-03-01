import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {Thumbnail} from '../../models/file/thumbnail.model';
import {StartedUploadEvent} from '../../models/upload-event.model';
import {FinishedUploadEvent} from '../../models/upload-event.model';

require('src/app/utilities/canvas-plus.js');
const canvas = new (window as any).CanvasPlus();

require('aws-sdk/dist/aws-sdk');
const AWS = (window as any).AWS;


@Component({
  selector: 'app-input-photo',
  templateUrl: './input-photo.component.html',
  styleUrls: ['./input-photo.component.css'],
})
export class InputPhotoComponent implements OnInit, ControlValueAccessor {

  readonly THUMBNAIL_SIZE = 256;
  readonly THUMBNAIL_QUALITY = 45;

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
    }
  };


  onChange = event => {};
  onTouched = () => {};


  constructor() { }

  ngOnInit(): void {
    this.bucket = new AWS.S3({params: {}});
  }


  addPhoto(uploadedFile: FinishedUploadEvent): void {
    const photo = new Photo(uploadedFile.url, null);

    // Attach photo and thumbnail
    const thumbnail = this.thumbnails.find(th => th.referencesPhoto(photo));
    if (thumbnail) {
      thumbnail.photo = photo;
      photo.thumbnails.add(thumbnail);
    }

    this.photos.push(photo);
  }


  /**
   * Generates a thumbnail, uploads it the AWS S3 server, and pushes it to this.thumbnails array
   * @param uploadingFile event from file-uploader
   */
  addThumbnail(uploadingFile: StartedUploadEvent): void {

    canvas.load( uploadingFile.file, (err1) => {
      if (err1) {
        console.error('Couldn\'t load file for thumbnail creation');
        return;
      }

      canvas.resize({
        width: this.THUMBNAIL_SIZE,
        mode: 'fit',
      });

      canvas.write({format: 'jpeg', quality: this.THUMBNAIL_QUALITY}, (err2, buf) => {
        if (err2) {
          console.error('Couldn\'t create thumbnail');
          return;
        }

        // 'buf' will be a binary buffer containing final image...
        const blob = new Blob( [ buf ], { type: 'image/jpeg' } );


        const bucketDir = uploadingFile.serverPath.slice(0, uploadingFile.serverPath.lastIndexOf('/'));
        const filename = Thumbnail.toThumbnailUrl(uploadingFile.serverPath).split('/').pop();

        this.uploadThumbnail(blob, bucketDir, filename);
      });

    });

  }

  /**
   * Upload thumbnail Blob to AWS S3 server.
   * @param thumbnailData Blob data of thumbnail
   * @param bucketDir the directory in the bucket where to put the file (not including file name).  i.e. bucketName/folder1/folder2
   * @param filename name of the file
   */
  uploadThumbnail(thumbnailData: Blob, bucketDir: string, filename: string): void {

    const params = {
      Bucket: bucketDir,
      Key: filename,
      Body: thumbnailData,
    };

    this.bucket.upload(params).send((err, data) => {

      if (err) {
        console.error('Could not upload thumbnail: ' + err.message);
        return;
      }

      const url = data.Location;
      const thumbnail = new Thumbnail(url, this.THUMBNAIL_SIZE);
      this.thumbnails.push(thumbnail);

      // Attach photo and thumbnail
      const photo = this.photos.find(p => thumbnail.referencesPhoto(p));
      if (photo) {
        thumbnail.photo = photo;
        photo.thumbnails.add(thumbnail);
      }

    });

  }


  // ------ControlValueAccessor implementations------

  writeValue(photos: Array<Photo>): void {
    if (photos) {
      this.photos = [...photos];
    }
    this.onChange(photos);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

}

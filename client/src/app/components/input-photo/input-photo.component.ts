import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';
import {Thumbnail} from '../../models/file/thumbnail.model';
import {FinishedUploadEvent} from '../../models/finished-upload-event.model';
import {StartedUploadEvent} from '../../models/started-upload-event.model';

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
  readonly THUMBNAIL_QUALITY = 35;

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

    // Attach to photo and thumbnail
    const thumbnail = this.thumbnails.filter(th => th.referencesPhoto(photo)).pop();
    if (thumbnail) {
      thumbnail.photo = photo;
      photo.thumbnails.add(thumbnail);
    }

    this.photos.push(photo);
    console.log('Photos', this.photos);
  }


  generateThumbnail(uploadingFile: StartedUploadEvent): void {

    canvas.load( uploadingFile.file, (err1) => {
      if (err1) { console.warn('Couldn\'t load file for thumbnail creation'); }

      canvas.resize({
        width: this.THUMBNAIL_SIZE,
        mode: 'fit',
      });

      canvas.write({format: 'jpeg', quality: this.THUMBNAIL_QUALITY}, (err2, buf) => {
        if (err2) { console.warn('Couldn\'t create thumbnail'); }

        // 'buf' will be a binary buffer containing final image...
        const blob = new Blob( [ buf ], { type: 'image/jpeg' } );

        // insert new image into DOM (Just for debugging)
        const objectUrl = URL.createObjectURL( blob );
        const img = new Image();
        img.src = objectUrl;
        img.height = 130;
        document.body.appendChild( img );


        const bucketDir = this.bucketDir(uploadingFile.serverPath);
        const fullThumbnailFilename = this.thumbnailFilename(uploadingFile.serverPath);

        this.uploadThumbnail(blob, bucketDir, fullThumbnailFilename);
      });

    });

  }

  /**
   * Upload Blob to AWS S3 server.
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
        console.warn('Could not upload thumbnail: ' + err.message);
      } else {
        const url = data.Location;
        const thumbnail = new Thumbnail(url, this.THUMBNAIL_SIZE);
        this.thumbnails.push(thumbnail);

        // Attach to photo and thumbnail
        const photo = this.photos.filter(p => p.referencesThumbnail(thumbnail)).pop();
        if (photo) {
          thumbnail.photo = photo;
          photo.thumbnails.add(thumbnail);
        }
      }
      console.log('Thumbs', this.thumbnails);
    });

  }


  /**
   * Splits a file name and its path.
   * @param serverPath path to a file; directory path and file name
   * @return the path to the file without the filename
   */
  bucketDir(serverPath: string): string {
    return serverPath.slice(0, serverPath.lastIndexOf('/'));
  }

  /**
   * Splits a file name and its path.  Adds a '_thumbnail' string to the filename before the file extension.
   * @param serverPath path to a file; directory path and file name
   * @return the filename
   */
  thumbnailFilename(serverPath: string): string {
    // Get bucketDir and thumbnail filename
    const dirPathArray = serverPath.split('/');
    const fullPhotoFilename = dirPathArray.pop();

    const dotIndex = fullPhotoFilename.lastIndexOf('.');
    return fullPhotoFilename.slice(0, dotIndex) + '_thumb' + fullPhotoFilename.slice(dotIndex);
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

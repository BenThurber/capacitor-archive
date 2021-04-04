import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends FileReference {

  order: number;
  capacitorUnitValue: string;
  thumbnails: Array<Thumbnail> = [];

  constructor(photo?: Photo) {
    super(photo);
    if (photo) {
      this.order = photo.order;
      this.capacitorUnitValue = photo.capacitorUnitValue;
      this.thumbnails = [];
      photo.thumbnails.forEach(thumb => this.thumbnails.push(new Thumbnail(thumb)));
    }
  }

  static fromUrl(url: string, order?: number): Photo {
    const photo = new Photo();
    photo.url = url;
    photo.order = order;
    return photo;
  }

  /**
   * Tries to get the url of a thumbnail.  If there are none, if falls back on its own Photo url.
   * @param maxSize the largest size that the thumbnail can be
   * @return a thumbnail url, or the photo's url
   */
  getThumbnailUrl(maxSize?: number): string {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return this.url;
    }

    let thumbnailArray = [...this.thumbnails];
    if (maxSize) {
      thumbnailArray = thumbnailArray.filter(th => (th.size <= maxSize));
    }

    const thumbnail = thumbnailArray.reduce((th1, th2) => th1.size > th2.size ? th1 : th2);

    return thumbnail.url;
  }

  /**
   * Tries to get the smallest thumbnail.
   * @return The smallest thumbnail in the Photo
   */
  getSmallestThumbnail(): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url, undefined);
    }

    // Get smallest thumbnail
    return this.thumbnails.reduce((th1, th2) => th1.size < th2.size ? th1 : th2);
  }

  /**
   * Tries to get the smallest thumbnail.
   * @return The smallest thumbnail in the Photo
   */
  getLargestThumbnail(): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url, undefined);
    }
    // Get largest thumbnail
    return this.thumbnails.reduce((th1, th2) => th1.size > th2.size ? th1 : th2);
  }



}

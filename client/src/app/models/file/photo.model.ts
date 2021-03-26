import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends FileReference {

  order: number;
  capacitorUnitValue: string;
  thumbnails: Set<Thumbnail> = new Set();

  constructor(photo?: Photo) {
    super(photo);
    if (photo) {
      this.order = photo.order;
      this.capacitorUnitValue = photo.capacitorUnitValue;
      this.thumbnails = new Set();
      photo.thumbnails.forEach(thumb => this.thumbnails.add(new Thumbnail(thumb)));
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
    if (this.thumbnails.size === 0) {
      return this.url;
    }

    let thumbnailArray = [...this.thumbnails];
    if (maxSize) {
      thumbnailArray = thumbnailArray.filter(th => (th.size <= maxSize));
    }

    const thumbnail = thumbnailArray.reduce((th1, th2) => th1.size > th2.size ? th1 : th2);

    return thumbnail.url;
  }

}

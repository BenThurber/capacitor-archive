import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';
import {closestSearch} from '../../utilities/closest-search';
import {min} from 'rxjs/operators';

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
   * Gets the smallest thumbnail.  Is there are none, it returns a new Thumbnail with the Photo's url.
   * @return The smallest thumbnail in the Photo
   */
  getSmallestThumbnail(): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url);
    }

    // Get smallest thumbnail
    return this.thumbnails.reduce((th1, th2) => th1.size < th2.size ? th1 : th2);
  }

  /**
   * Gets the largest thumbnail.  Is there are none, it returns a new Thumbnail with the Photo's url.
   * @return The smallest thumbnail in the Photo
   */
  getLargestThumbnail(): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url);
    }

    // Get largest thumbnail
    return this.thumbnails.reduce((th1, th2) => th1.size > th2.size ? th1 : th2);
  }

  /**
   * Gets the thumbnail closest in size to targetSize.  If minimumSize is provided, then the closest thumbnail size is smaller
   * than minimumSize, a new thumbnail with the url of the photo is returned.
   * @return The thumbnail closest in size to targetSize.
   */
  getThumbnail(targetSize: number, minimumSize?: number): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url);
    }

    let closestThumbnail = closestSearch(targetSize, this.thumbnails, (th: Thumbnail) => th.size);

    if (minimumSize && closestThumbnail.size < minimumSize) {
      closestThumbnail = this.getLargestThumbnail();
      if (closestThumbnail.size < minimumSize) {
        return Thumbnail.fromUrl(this.url);
      } else {
        return closestThumbnail;
      }
    }

    return closestThumbnail;
  }



}

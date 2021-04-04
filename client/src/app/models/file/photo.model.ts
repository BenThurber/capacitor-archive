import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';
import {closestSearch} from '../../utilities/closest-search';

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
   * Tries to get the smallest thumbnail.
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
   * Tries to get the smallest thumbnail.
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
   * Gets the thumbnail closest in size to targetSize.  If plusOrMinus is provided, then if (target - plusOrMinus) is greater
   * than the closest thumbnail, a new thumbnail with the url of the photo is returned.
   * @return The thumbnail closest in size to targetSize.
   */
  getThumbnail(targetSize: number, minimumSize?: number): Thumbnail {
    if (!this.thumbnails || this.thumbnails.length === 0) {
      return Thumbnail.fromUrl(this.url);
    }

    const closestThumbnail = closestSearch(targetSize, this.thumbnails, (th: Thumbnail) => th.size);

    if (minimumSize && minimumSize > closestThumbnail.size) {
      return Thumbnail.fromUrl(this.url);
    }

    return closestThumbnail;
  }



}

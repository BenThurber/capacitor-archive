import {FileReference} from './file-reference.model';
import {Photo} from './photo.model';

export class Thumbnail extends FileReference {

  constructor(thumbnail?: Thumbnail) {
    super(thumbnail);
    if (thumbnail) {
      this.size = thumbnail.size;
    }
  }

  size: number;
  photo: Photo;

  static fromUrl(url: string, size?: number): Thumbnail {
    const thumbnail = new Thumbnail();
    thumbnail.url = url;
    thumbnail.size = size;
    return thumbnail;
  }

  /**
   * Takes a full or partial Photo url and converts it to a Thumbnail url.  Adds a '_thumb' substring and changes
   * the extension to '.jpg'.
   * @param url a url to a photo
   * @return the url of the thumbnail
   */
  static toThumbnailUrl(url: string): string {
    const dotIndex = url.lastIndexOf('.');
    return url.slice(0, dotIndex) + '_thumb.jpg';
  }

  /**
   * Determines if this thumbnail can be attached to the given photo
   * @param photo a photo object to test
   */
  referencesPhoto(photo: Photo): boolean {
    if (this.photo === photo) {
      return true;
    }

    // Compares URLs.  Needs to replace '+' with '%20' and decode URLs (for some reason decodeURIComponent doesn't decode spaces!?)
    return decodeURIComponent(this.url.replace(/\+/g, '%20')) ===
      decodeURIComponent(Thumbnail.toThumbnailUrl(photo.url.replace(/\+/g, '%20')));
  }

}

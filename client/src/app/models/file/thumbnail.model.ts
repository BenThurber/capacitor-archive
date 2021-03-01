import {FileReference} from './file-reference.model';
import {Photo} from './photo.model';

export class Thumbnail extends FileReference {

  constructor(url: string, size: number) {
    super(url);
    this.size = size;
  }

  size: number;
  photo: Photo;

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


  referencesPhoto(photo: Photo): boolean {
    if (this.photo === photo) {
      return true;
    }

    return this.url === Thumbnail.toThumbnailUrl(photo.url);
  }

}

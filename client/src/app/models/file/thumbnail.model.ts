import {FileReference} from './file-reference.model';
import {Photo} from './photo.model';

export class Thumbnail extends FileReference {

  constructor(url: string, size: number) {
    super(url);
    this.size = size;
  }

  size: number;
  photo: Photo;

  referencesPhoto(photo: Photo): boolean {
    if (this.photo === photo) {
      return true;
    }

    const index = this.url.lastIndexOf('_thumb');
    const photoUrl = this.url.slice(0, index) + this.url.slice(index + 6, this.url.length);

    return photoUrl === photo.url;
  }

}

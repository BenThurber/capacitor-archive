import {FileReference} from './file-reference.model';
import {Photo} from './photo.model';

export class Thumbnail extends FileReference {

  constructor(url: string, size: number) {
    super(url);
    this.size = size;
  }

  size: number;
  photo: Photo;

}

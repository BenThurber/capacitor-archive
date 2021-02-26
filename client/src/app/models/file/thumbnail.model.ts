import {File} from './file.model';
import {Photo} from './photo.model';

export class Thumbnail extends File {

  constructor(url: string, size: number) {
    super(url);
    this.size = size;
  }

  size: number;
  photo: Photo;

}

import {File} from './file.model';
import {Photo} from './photo.model';

export class Thumbnail extends File {

  size: number;
  photo: Photo;

}

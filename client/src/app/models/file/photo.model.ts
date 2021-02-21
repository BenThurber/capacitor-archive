import {File} from './file.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends File {

  order: number;
  thumbnails: Array<Thumbnail>;

}

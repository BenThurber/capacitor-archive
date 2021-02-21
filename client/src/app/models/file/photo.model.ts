import {File} from './file.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends File {

  order: number;
  capacitorUnitValue: string;
  thumbnails: Array<Thumbnail>;

}

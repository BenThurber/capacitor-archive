import {File} from './file.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends File {

  constructor(url: string, order: number) {
    super(url);
    this.order = order;
  }

  order: number;
  capacitorUnitValue: string;
  thumbnails: Array<Thumbnail>;

}

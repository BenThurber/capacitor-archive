import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends FileReference {

  constructor(url: string, order: number) {
    super(url);
    this.order = order;
  }

  order: number;
  capacitorUnitValue: string;
  thumbnails: Array<Thumbnail>;

}

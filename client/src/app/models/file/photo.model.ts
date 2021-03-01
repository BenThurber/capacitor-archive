import {FileReference} from './file-reference.model';
import {Thumbnail} from './thumbnail.model';

export class Photo extends FileReference {

  constructor(url: string, order: number) {
    super(url);
    this.order = order;
  }

  order: number;
  capacitorUnitValue: string;
  thumbnails: Set<Thumbnail> = new Set();

  referencesThumbnail(thumbnail: Thumbnail): boolean {
    if (this.thumbnails.has(thumbnail)) {
      return true;
    }

    const index = thumbnail.url.lastIndexOf('_thumb');
    const photoUrl = thumbnail.url.slice(0, index) + thumbnail.url.slice(index + 6, thumbnail.url.length);

    return photoUrl === this.url;
  }

}

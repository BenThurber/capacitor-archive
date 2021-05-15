import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

/**
 * Updates the navigational breadcrumb, i.e.   Home > Solar > ...
 */
@Injectable({
  providedIn: 'root'
})
export class BreadcrumbService {

  private changeAnnouncedSource = new Subject<any>();

  /**
   * Should be subscribed to by components that display manufacturer data like ManufacturerSidebar
   */
  public changeAnnounced$ = this.changeAnnouncedSource.asObservable();

  constructor() { }

  /**
   * Should be called when the list of manufacturers are changed
   * @param links an Array of Objects {name: string, url: Array<string>}
   * where the name is the string to display in the breadcrumb.
   */
  public change(links: Array<{name: string, url: Array<string>}>): void {
    this.changeAnnouncedSource.next(links);
  }

}

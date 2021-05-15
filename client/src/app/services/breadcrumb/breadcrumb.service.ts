import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';


/**
 * Update the breadcrumb navigation on a particular page.
 */
export interface UpdateBreadcrumb {
  updateBreadcrumb(...args: any[]): void;
}

/**
 * Updates the navigational breadcrumb, i.e.   Home > Solar > ...
 */
@Injectable({
  providedIn: 'root'
})
export class BreadcrumbService {

  private changeAnnouncedSource = new Subject<any>();

  /**
   * Should be subscribed to by NavigationBreadcrumbComponent
   */
  public changeAnnounced$ = this.changeAnnouncedSource.asObservable();

  constructor() { }

  /**
   * Should be called by View pages that use the breadcrumb.
   * @param links an Array of Objects {name: string, url: Array<string>}
   * where the name is the string to display in the breadcrumb.
   */
  public change(links: Array<{name: string, url: Array<string>}>): void {
    this.changeAnnouncedSource.next(links);
  }

}

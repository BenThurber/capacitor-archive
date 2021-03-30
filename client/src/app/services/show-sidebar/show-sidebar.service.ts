import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ShowSidebarService {

  private showSidebarAnnouncedSource = new Subject<any>();


  public showSidebarAnnounced$ = this.showSidebarAnnouncedSource.asObservable();

  /**
   * Called when the manufacturer sidebar needs to be emphisized
   * @param shown boolean true if sidebar is shown
   */
  public change(shown: boolean): void {
    this.showSidebarAnnouncedSource.next(shown);
  }


}

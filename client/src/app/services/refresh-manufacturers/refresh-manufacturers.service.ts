import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RefreshManufacturersService {

  private refreshAnnouncedSource = new Subject<any>();


  /**
   * Should be subscribed to by components that display manufacturer data like ManufacturerSidebar
   */
  public refreshAnnounced$ = this.refreshAnnouncedSource.asObservable();

  /**
   * Should be called when the list of manufacturers are changed
   */
  public refresh(): void {
    this.refreshAnnouncedSource.next();
  }
}

import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RefreshManufacturersService {

  private refreshAnnouncedSource = new Subject<void>();
  private manufacturerAddedCapacitorUnitSource = new Subject<string>();
  private manufacturerAddedCapacitorTypeSource = new Subject<string>();


  /**
   * Should be subscribed to by components that display manufacturer data like ManufacturerSidebar
   */
  public refreshAnnounced$ = this.refreshAnnouncedSource.asObservable();

  public manufacturerAddedCapacitorUnit$ = this.manufacturerAddedCapacitorUnitSource.asObservable();
  public manufacturerAddedCapacitorType$ = this.manufacturerAddedCapacitorTypeSource.asObservable();

  /**
   * Should be called when the list of manufacturers are changed
   */
  public refresh(): void {
    this.refreshAnnouncedSource.next();
  }

  /**
   * Called when a new capacitor has been created.  Indicates that a new capacitor has been added.
   */
  public addedNewCapacitorUnit(companyName: string): void {
    this.manufacturerAddedCapacitorUnitSource.next(companyName);
  }

  /**
   * Called when a new capacitor has been created.  Indicates that a new capacitor has been added.
   */
  public addedNewCapacitorType(companyName: string): void {
    this.manufacturerAddedCapacitorTypeSource.next(companyName);
  }
}

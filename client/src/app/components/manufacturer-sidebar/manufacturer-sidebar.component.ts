import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {RefreshManufacturersService} from '../../services/refresh-manufacturers/refresh-manufacturers.service';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {ShowSidebarService} from '../../services/show-sidebar/show-sidebar.service';
import {ManufacturerListItem} from '../../models/manufacturer-list-item';


@Component({
  selector: 'app-manufacturer-sidebar',
  templateUrl: './manufacturer-sidebar.component.html',
  styleUrls: ['./manufacturer-sidebar.component.css', '../../styles/animations.css']
})
export class ManufacturerSidebarComponent implements OnInit {

  manufacturerListItems: Array<ManufacturerListItem>;

  showShadow = null;

  constructor(private restService: RestService, private activatedRoute: ActivatedRoute,
              private refreshManufacturers: RefreshManufacturersService, private showSidebarService: ShowSidebarService) {

    // Reload the component when a refresh is announced
    this.refreshManufacturers.refreshAnnounced$.subscribe(() => this.ngOnInit());
    this.showSidebarService.showSidebarAnnounced$.subscribe(shown => this.showShadow = shown);
  }

  ngOnInit(): Subscription {
    return this.restService.getAllManufacturerListItems().subscribe({
      next: manufacturerListItems => {
        manufacturerListItems.sort((li1, li2) => caseInsensitiveCompare(li1.companyName, li2.companyName));
        this.manufacturerListItems = manufacturerListItems;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }

  selectAnimation(showShadow: boolean): string {
    if (showShadow === true) {
      return 'show-sidebar';
    } else if (showShadow === false) {
      return 'hide-sidebar';
    } else {
      return '';
    }
  }

  toolTipMsg(numCapacitorUnits): string {
    if (numCapacitorUnits <= 0) {
      return 'No Capacitors';
    } else if (numCapacitorUnits === 1) {
      return numCapacitorUnits + ' Capacitor';
    } else {
      return numCapacitorUnits + ' Capacitors';
    }
  }

}

import { Component, OnInit } from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';

@Component({
  selector: 'app-view-manufacturer',
  templateUrl: './view-manufacturer.component.html',
  styleUrls: ['./view-manufacturer.component.css', '../../styles/animations.css']
})
export class ViewManufacturerComponent implements OnInit {

  companyName: string;
  manufacturer$: Manufacturer;
  capacitorTypesSearchResponseObservable: Observable<Array<CapacitorTypeSearchResponse>>;

  constructor(public restService: RestService, public dynamicRouter: DynamicRouterService, activatedRoute: ActivatedRoute) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.capacitorTypesSearchResponseObservable = this.restService.getAllTypeSearchResponses(this.companyName);
  }

  ngOnInit(): Subscription {

    return this.restService.getManufacturerByName(this.companyName)
      .subscribe((manufacturer: Manufacturer) => this.manufacturer$ = manufacturer);
  }

}

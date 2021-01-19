import { Component, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {CapacitorType} from '../../models/capacitor-type.model';

@Component({
  selector: 'app-view-manufacturer',
  templateUrl: './view-manufacturer.component.html',
  styleUrls: ['./view-manufacturer.component.css']
})
export class ViewManufacturerComponent implements OnInit {

  companyName: string;
  manufacturer$: Manufacturer;
  types: Array<CapacitorType>;

  constructor(private restService: RestService, public dynamicRouter: DynamicRouterService, activatedRoute: ActivatedRoute) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
  }

  ngOnInit(): Subscription {
    this.restService.getAllTypes(this.companyName).subscribe((types: Array<CapacitorType>) => this.types = types);

    return this.restService.getManufacturerByName(this.companyName)
      .subscribe((manufacturer: Manufacturer) => this.manufacturer$ = manufacturer);
  }

}

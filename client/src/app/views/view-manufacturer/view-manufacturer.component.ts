import { Component, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest.service';

@Component({
  selector: 'app-view-manufacturer',
  templateUrl: './view-manufacturer.component.html',
  styleUrls: ['./view-manufacturer.component.css']
})
export class ViewManufacturerComponent implements OnInit {

  manufacturer$: Manufacturer;
  restService: RestService;

  constructor(restService: RestService) {
    this.restService = restService;
  }

  ngOnInit(): Subscription {
    return this.restService.getManufacturerById(1).subscribe(manufacturer => this.manufacturer$ = manufacturer);
  }

}

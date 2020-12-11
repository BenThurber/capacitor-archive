import { Component, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-view-manufacturer',
  templateUrl: './view-manufacturer.component.html',
  styleUrls: ['./view-manufacturer.component.css']
})
export class ViewManufacturerComponent implements OnInit {

  manufacturerName: string;
  manufacturer$: Manufacturer;

  restService: RestService;

  constructor(restService: RestService, activatedRoute: ActivatedRoute) {
    this.restService = restService;
    this.manufacturerName = activatedRoute.snapshot.paramMap.get('manufacturerName');
  }

  ngOnInit(): Subscription {
    return this.restService.getManufacturerByName(this.manufacturerName).subscribe(manufacturer => this.manufacturer$ = manufacturer);
  }

}

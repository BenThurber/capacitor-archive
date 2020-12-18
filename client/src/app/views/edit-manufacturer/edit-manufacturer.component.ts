import { Component, OnInit } from '@angular/core';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-edit-manufacturer',
  templateUrl: './edit-manufacturer.component.html',
  styleUrls: ['./edit-manufacturer.component.css']
})
export class EditManufacturerComponent implements OnInit {

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

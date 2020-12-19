import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
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

  companyName: string;
  manufacturer$: Manufacturer;

  restService: RestService;
  changeDetectorRef: ChangeDetectorRef;

  constructor(restService: RestService, activatedRoute: ActivatedRoute, changeDetectorRef: ChangeDetectorRef) {
    this.restService = restService;
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.changeDetectorRef = changeDetectorRef;
  }

  ngOnInit(): Subscription {
    return this.restService.getManufacturerByName(this.companyName).subscribe({
      next: manufacturer => {
        this.manufacturer$ = manufacturer;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

}
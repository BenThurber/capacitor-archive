import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestService} from '../../services/rest/rest.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';

@Component({
  selector: 'app-view-capacitor',
  templateUrl: './view-capacitor.component.html',
  styleUrls: ['./view-capacitor.component.css']
})
export class ViewCapacitorComponent implements OnInit {

  companyName: string;
  typeName: string;
  value: string;

  capacitorType: CapacitorType;
  capacitorUnit: CapacitorUnit;
  capacitorUnits: Array<CapacitorUnit>;

  constructor(private activatedRoute: ActivatedRoute, private restService: RestService) {
    this.companyName = this.activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = this.activatedRoute.snapshot.paramMap.get('typeName');
    this.value = this.activatedRoute.snapshot.paramMap.get('value');
  }

  ngOnInit(): any {

    if (this.value) {
      this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value)
        .subscribe((capacitorUnit: CapacitorUnit) => this.capacitorUnit = capacitorUnit);
    }

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName)
      .subscribe((capacitorType: CapacitorType) => this.capacitorType = capacitorType);

    return this.restService.getAllCapacitorUnitsFromCapacitorType(this.companyName, this.typeName)
      .subscribe((capacitorUnits: Array<CapacitorUnit>) => {
        this.capacitorUnits = capacitorUnits.sort();
      });
  }

}



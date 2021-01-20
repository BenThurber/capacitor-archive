import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {RestService} from '../../services/rest/rest.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {padEndHtml} from '../../utilities/text-utils';
import {Manufacturer} from '../../models/manufacturer.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';

@Component({
  selector: 'app-view-capacitor',
  templateUrl: './view-capacitor.component.html',
  styleUrls: ['./view-capacitor.component.css', '../../styles/animations.css']
})
export class ViewCapacitorComponent implements OnInit {

  @ViewChild('similarMenu') similarMenu: ElementRef;

  companyName: string;
  typeName: string;
  value: string;

  capacitorType: CapacitorType;
  capacitorUnit: CapacitorUnit;
  capacitorUnits: Array<CapacitorUnit>;
  manufacturer: Manufacturer;

  formattedCapacitance = CapacitorUnit.formattedCapacitance;


  constructor(private activatedRoute: ActivatedRoute, private restService: RestService,  public dynamicRouter: DynamicRouterService) {
    this.companyName = this.activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = this.activatedRoute.snapshot.paramMap.get('typeName');
    this.value = this.activatedRoute.snapshot.paramMap.get('value');
  }

  ngOnInit(): any {

    if (this.value) {
      this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value)
        .subscribe((capacitorUnit: CapacitorUnit) => {
          this.capacitorUnit = capacitorUnit;
          // Set focus on the similar menu
          setTimeout(() => this.similarMenu.nativeElement.focus(), 100);
        });
    }

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName)
      .subscribe((capacitorType: CapacitorType) => this.capacitorType = capacitorType);

    this.restService.getManufacturerByName(this.companyName).subscribe(
      (manufacturer: Manufacturer) => this.manufacturer = manufacturer);

    return this.restService.getAllCapacitorUnitsFromCapacitorType(this.companyName, this.typeName)
      .subscribe((capacitorUnits: Array<CapacitorUnit>) => {
        this.capacitorUnits = capacitorUnits.sort(CapacitorUnit.compare);
        if (!this.value && this.capacitorUnits.length > 0) {
          this.capacitorUnit = this.capacitorUnits[0];
        } else if (this.capacitorUnits.length === 0) {
          this.capacitorUnit = new CapacitorUnit();
        }
        // Set focus on the similar menu
        setTimeout(() => this.similarMenu.nativeElement.focus(), 100);
      });
  }


  similarMenuChanged(value): void {
    this.capacitorUnit = this.capacitorUnits.filter(u => u.value === value).pop();
  }

  formatSimilarCapacitor(capacitorUnit: CapacitorUnit): string {
    let str = '';
    str += padEndHtml(CapacitorUnit.formattedCapacitance(capacitorUnit.capacitance, true)
      .replace(' ', ''), 9);
    str += padEndHtml(String(capacitorUnit.voltage > 0 ? capacitorUnit.voltage + 'V' : ''), 8);
    str += capacitorUnit.identifier ? capacitorUnit.identifier : '';

    return str;
  }

}



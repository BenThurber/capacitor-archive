import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {Title} from '@angular/platform-browser';
import {title} from '../../utilities/text-utils';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';

@Component({
  selector: 'app-edit-capacitor',
  templateUrl: './edit-capacitor.component.html',
  styleUrls: ['./edit-capacitor.component.css']
})
export class EditCapacitorComponent implements OnInit {

  companyName: string;
  typeName: string;
  value: string;

  capacitorType: CapacitorType;
  capacitorUnit: CapacitorUnit;
  formattedCapacitance = '';

  constructor(private titleService: Title, private restService: RestService, private activatedRoute: ActivatedRoute,
              public dynamicRouter: DynamicRouterService, private errorHandler: ErrorHandlerService) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = activatedRoute.snapshot.paramMap.get('typeName');
    this.value = activatedRoute.snapshot.paramMap.get('value');
  }

  ngOnInit(): void {
    this.titleService.setTitle('Editing ' + title(this.typeName) + (this.value ? ' ' + this.value : ''));

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName).subscribe({
      next: (capacitorType: CapacitorType) => this.capacitorType = capacitorType,
      error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorType to edit.'),
    });
    this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value).subscribe({
      next: (capacitorUnit: CapacitorUnit) => {
        this.capacitorUnit = capacitorUnit;
        this.formattedCapacitance = CapacitorUnit.formattedCapacitance(capacitorUnit.capacitance, true);
      },
      error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorUnit to edit'),
    });
  }

}

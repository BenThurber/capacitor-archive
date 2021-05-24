import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {Title} from '@angular/platform-browser';
import {title} from '../../utilities/text-utils';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';
import {BreadcrumbService, UpdateBreadcrumb} from '../../services/breadcrumb/breadcrumb.service';

@Component({
  selector: 'app-edit-capacitor',
  templateUrl: './edit-capacitor.component.html',
  styleUrls: ['./edit-capacitor.component.css']
})
export class EditCapacitorComponent implements OnInit, UpdateBreadcrumb {

  companyName: string;
  typeName: string;
  value: string;

  only: 'type' | 'unit' | 'photos';

  capacitorType: CapacitorType;
  capacitorUnit: CapacitorUnit;
  formattedCapacitance = '';

  constructor(private titleService: Title, private restService: RestService, private activatedRoute: ActivatedRoute,
              public dynamicRouter: DynamicRouterService, private errorHandler: ErrorHandlerService,
              private breadcrumbService: BreadcrumbService) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = activatedRoute.snapshot.paramMap.get('typeName');
    this.value = activatedRoute.snapshot.paramMap.get('value');
    this.only = activatedRoute.snapshot.queryParamMap.get('only') as any;
  }

  ngOnInit(): void {
    this.titleService.setTitle('Editing ' + title(this.typeName) + (this.value ? ' ' + this.value : ''));

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName).subscribe({
      next: (ct: CapacitorType) => {
        this.capacitorType = ct;
        if (this.only === 'type') {
          this.updateBreadcrumb(ct.companyName, ct.typeName);
        }
      },
      error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorType to edit.'),
    });
    if (this.value) {
      this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value).subscribe({
        next: (cu: CapacitorUnit) => {
          this.capacitorUnit = cu;
          this.formattedCapacitance = CapacitorUnit.formattedCapacitance(cu.capacitance, true);
          this.updateBreadcrumb(cu.companyName, cu.typeName, cu.value, this.formattedCapacitance);
        },
        error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorUnit to edit'),
      });
    }
  }

  updateBreadcrumb(companyName: string, typeName: string, value?: string, formattedCapacitance?: string): void {
    const links = [
      {name: companyName,
        url: ['/manufacturer', 'view', companyName.toLowerCase()]
      },
      {name: typeName,
        url: ['/capacitor', 'view', companyName.toLowerCase(), typeName.toLowerCase()]
      },
    ];
    if (value && formattedCapacitance) {
      links.push(
        {name: 'Editing ' + formattedCapacitance,
          url: ['/capacitor', 'edit', companyName.toLowerCase(), typeName.toLowerCase(), value]
        },
      );
    } else {
      links[1].name = 'Editing ' + typeName;
    }
    this.breadcrumbService.change(links);
  }
  //
  // get editingType(): boolean {
  //   return Boolean(this.companyName && this.capacitorType && !this.capacitorUnit && !this.value);
  // }
  //
  // get editingUnit(): boolean {
  //   return Boolean(this.companyName && this.capacitorType && this.capacitorUnit);
  // }

}

import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {CapacitorType} from '../../models/capacitor-type.model';
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
              public router: Router, private errorHandler: ErrorHandlerService,
              private breadcrumbService: BreadcrumbService) {
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.typeName = activatedRoute.snapshot.paramMap.get('typeName');
    this.value = activatedRoute.snapshot.paramMap.get('value');
    this.only = activatedRoute.snapshot.queryParamMap.get('only') as any;
    if (!['type', 'unit', 'photos'].includes(this.only)) {
      this.only = null;
    }
  }

  ngOnInit(): void {
    this.titleService.setTitle('Editing ' + title(this.typeName) + (this.value ? ' ' + this.value : ''));

    this.restService.getCapacitorTypeByName(this.companyName, this.typeName).subscribe({
      next: (ct: CapacitorType) => {
        this.capacitorType = ct;
        if (this.only === 'type') {
          this.updateBreadcrumb(ct.companyName, ct.typeName, this.value);
        }
      },
      error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorType to edit.'),
    });
    if (this.value) {
      this.restService.getCapacitorUnitByValue(this.companyName, this.typeName, this.value).subscribe({
        next: (cu: CapacitorUnit) => {
          this.capacitorUnit = cu;
          this.formattedCapacitance = CapacitorUnit.formattedCapacitance(cu.capacitance, true);
          if (this.only !== 'type') {
            this.updateBreadcrumb(cu.companyName, cu.typeName, cu.value, this.formattedCapacitance);
          }
        },
        error: err => this.errorHandler.handleGetRequestError(err, 'Could not get CapacitorUnit to edit'),
      });
    }
  }

  updateBreadcrumb(companyName: string, typeName: string, value?: string, formattedCapacitance?: string): void {
    const links = [
      {name: companyName,
        url: ['/manufacturer', 'view', companyName]
      },
      {name: typeName,
        url: ['/capacitor', 'view', companyName, typeName]
      },
    ];
    // Editing unit
    if (value && formattedCapacitance) {
      links.push(
        {name: 'Editing ' + formattedCapacitance,
          url: ['/capacitor', 'edit', companyName, typeName, value],
          params: {only: 'unit'},
        } as any,
      );
      links[1].url.push(value);

    // Editing only type
    } else {
      links.push(
        {name: 'Editing ' + typeName,
          url: ['/capacitor', 'edit', companyName, typeName],
          params: {only: 'type'},
        } as any,
      );
      if (value) {
        links[links.length - 1].url.push(value);
      }
    }
    this.breadcrumbService.change(links);
  }

}

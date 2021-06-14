import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';
import {Title} from '@angular/platform-browser';
import {title} from '../../utilities/text-utils';
import {ErrorHandlerService} from '../../services/error-handler/error-handler.service';
import {BreadcrumbService, UpdateBreadcrumb} from '../../services/breadcrumb/breadcrumb.service';

@Component({
  selector: 'app-view-manufacturer',
  templateUrl: './view-manufacturer.component.html',
  styleUrls: ['./view-manufacturer.component.css', '../../styles/animations.css']
})
export class ViewManufacturerComponent implements OnInit, UpdateBreadcrumb {

  companyName: string;
  manufacturer$: Manufacturer;
  capacitorTypesSearchResponseObservable: Observable<Array<CapacitorTypeSearchResponse>>;

  constructor(private titleService: Title, public restService: RestService, public router: Router,
              public activatedRoute: ActivatedRoute, private errorHandler: ErrorHandlerService,
              private breadcrumbService: BreadcrumbService) {
  }

  ngOnInit(): void {

    this.activatedRoute.paramMap.subscribe(() => {
      this.manufacturer$ = null;

      this.companyName = this.activatedRoute.snapshot.paramMap.get('companyName');
      this.titleService.setTitle('Viewing ' + title(this.companyName));
      this.capacitorTypesSearchResponseObservable = this.restService.getAllTypeSearchResponses(this.companyName);

      this.restService.getManufacturerByName(this.companyName)
        .subscribe({
          next: (manufacturer: Manufacturer) => {
            this.manufacturer$ = manufacturer;
            this.updateBreadcrumb(manufacturer.companyName);
          },
          error: err => this.errorHandler.handleGetRequestError(err, 'Error getting manufacturer')
        });
    });
  }

  updateBreadcrumb(companyName: string): void {
    this.breadcrumbService.change([
      {name: companyName,
        url: ['/manufacturer', 'view', companyName]
      }
    ]);
  }

}

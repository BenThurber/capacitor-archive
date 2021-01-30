import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {RefreshManufacturersService} from '../../services/refresh-manufacturers/refresh-manufacturers.service';
import {caseInsensitiveCompare} from '../../utilities/text-utils';

@Component({
  selector: 'app-manufacturer-sidebar',
  templateUrl: './manufacturer-sidebar.component.html',
  styleUrls: ['./manufacturer-sidebar.component.css', '../../styles/animations.css']
})
export class ManufacturerSidebarComponent implements OnInit {

  companyNames$: Array<string>;

  restService: RestService;
  dynamicRouter: DynamicRouterService;
  refreshManufacturers: RefreshManufacturersService;


  constructor(restService: RestService, activatedRoute: ActivatedRoute, dynamicRouter: DynamicRouterService,
              refreshManufacturers: RefreshManufacturersService) {
    this.restService = restService;
    this.dynamicRouter = dynamicRouter;
    this.refreshManufacturers = refreshManufacturers;

    // Reload the component when a refresh is announced
    this.refreshManufacturers.refreshAnnounced$.subscribe(() => this.ngOnInit());
  }

  ngOnInit(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: companyNames => {
        companyNames.sort(caseInsensitiveCompare);
        this.companyNames$ = companyNames;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }

}

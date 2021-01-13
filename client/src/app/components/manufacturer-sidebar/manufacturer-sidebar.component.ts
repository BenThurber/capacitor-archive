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
  styleUrls: ['./manufacturer-sidebar.component.css']
})
export class ManufacturerSidebarComponent implements OnInit {

  companyName: string;
  manufacturers$: Array<string>;

  restService: RestService;
  dynamicRouter: DynamicRouterService;
  refreshManufacturers: RefreshManufacturersService;


  constructor(restService: RestService, activatedRoute: ActivatedRoute, dynamicRouter: DynamicRouterService,
              refreshManufacturers: RefreshManufacturersService) {
    this.restService = restService;
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
    this.dynamicRouter = dynamicRouter;
    this.refreshManufacturers = refreshManufacturers;

    // Reload the component when a refresh is announced
    this.refreshManufacturers.refreshAnnounced$.subscribe(() => this.ngOnInit());
  }

  ngOnInit(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: manufacturers => {
        manufacturers.sort(caseInsensitiveCompare);
        this.manufacturers$ = manufacturers;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }

}

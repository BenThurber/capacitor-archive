import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {RefreshManufacturersService} from '../../services/refresh-manufacturers/refresh-manufacturers.service';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {ShowSidebarService} from '../../services/show-sidebar/show-sidebar.service';


@Component({
  selector: 'app-manufacturer-sidebar',
  templateUrl: './manufacturer-sidebar.component.html',
  styleUrls: ['./manufacturer-sidebar.component.css', '../../styles/animations.css']
})
export class ManufacturerSidebarComponent implements OnInit {

  companyNames$: Array<string>;

  showShadow = null;

  dynamicRouter: DynamicRouterService;

  constructor(private restService: RestService, private activatedRoute: ActivatedRoute, dynamicRouter: DynamicRouterService,
              private refreshManufacturers: RefreshManufacturersService, private showSidebarService: ShowSidebarService) {

    this.dynamicRouter = dynamicRouter;

    // Reload the component when a refresh is announced
    this.refreshManufacturers.refreshAnnounced$.subscribe(() => this.ngOnInit());
    this.showSidebarService.showSidebarAnnounced$.subscribe(shown => this.showShadow = shown);
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

  selectAnimation(showShadow: boolean): string {
    if (showShadow === true) {
      return 'show-sidebar';
    } else if (showShadow === false) {
      return 'hide-sidebar';
    } else {
      return '';
    }
  }

}

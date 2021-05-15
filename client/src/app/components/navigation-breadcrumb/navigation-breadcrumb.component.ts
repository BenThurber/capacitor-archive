import {Component, Input, OnInit} from '@angular/core';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {BreadcrumbService} from '../../services/breadcrumb/breadcrumb.service';
import {NavigationStart} from '@angular/router';

@Component({
  selector: 'app-navigation-breadcrumb',
  templateUrl: './navigation-breadcrumb.component.html',
  styleUrls: ['./navigation-breadcrumb.component.css']
})
export class NavigationBreadcrumbComponent implements OnInit {

  @Input() links: Array<{name: string, url: Array<string>}> = [{name: 'Solarus', url: ['/about']}, {name: '0.1 uf', url: ['/contact']}];

  constructor(public dynamicRouter: DynamicRouterService, private breadcrumbService: BreadcrumbService) {

    this.breadcrumbService.changeAnnounced$.subscribe(links => {
      links.unshift({name: 'Home', url: ['/']});
      this.links = links;
    });
  }

  ngOnInit(): void {
    this.dynamicRouter.router.events
      .subscribe((event: NavigationStart) => {
        // Clear breadcrumb on page navigation
        this.links = [];
      });
  }

}

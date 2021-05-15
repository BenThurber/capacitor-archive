import {Component, Input, OnInit} from '@angular/core';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {BreadcrumbService} from '../../services/breadcrumb/breadcrumb.service';
import {NavigationStart} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-navigation-breadcrumb',
  templateUrl: './navigation-breadcrumb.component.html',
  styleUrls: ['./navigation-breadcrumb.component.css']
})
export class NavigationBreadcrumbComponent implements OnInit {

  @Input() links: Array<{name: string, url: Array<string>}>;

  constructor(public dynamicRouter: DynamicRouterService, private breadcrumbService: BreadcrumbService) {

    this.breadcrumbService.changeAnnounced$.subscribe(links => {
      links.unshift({name: 'Home', url: ['/']});
      this.links = links;
    });
  }

  ngOnInit(): void {
    this.dynamicRouter.router.events.pipe(
      filter(event => event instanceof NavigationStart),
    ).subscribe(() => this.links = []);
  }

}

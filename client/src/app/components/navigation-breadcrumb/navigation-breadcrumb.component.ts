import {Component, Input, OnInit} from '@angular/core';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';

@Component({
  selector: 'app-navigation-breadcrumb',
  templateUrl: './navigation-breadcrumb.component.html',
  styleUrls: ['./navigation-breadcrumb.component.css']
})
export class NavigationBreadcrumbComponent implements OnInit {

  @Input() links: Array<{name: string, url: Array<string>}> = [{name: 'Solarus', url: ['/about']}, {name: '0.1 uf', url: ['/contact']}];

  constructor(public dynamicRouter: DynamicRouterService) { }

  ngOnInit(): void {
  }

}

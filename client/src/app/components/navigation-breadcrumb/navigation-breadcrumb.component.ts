import {Component, Input, OnInit} from '@angular/core';
import {BreadcrumbService} from '../../services/breadcrumb/breadcrumb.service';
import {NavigationStart, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

/**
 * A breadcrumb navigator, i.e.   Home > Solar > ...
 */
@Component({
  selector: 'app-navigation-breadcrumb',
  templateUrl: './navigation-breadcrumb.component.html',
  styleUrls: ['./navigation-breadcrumb.component.css']
})
export class NavigationBreadcrumbComponent implements OnInit {

  @Input() links: Array<{name: string, url: Array<string>, params?: object}> = [];

  constructor(public router: Router, private breadcrumbService: BreadcrumbService) {

    this.breadcrumbService.changeAnnounced$.subscribe(links => {
      links.unshift({name: 'Home', url: ['/']});
      this.links = links;
    });
  }

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationStart),
    ).subscribe(() => this.links = []);
  }

}

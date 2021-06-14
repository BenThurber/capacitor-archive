import {Directive, Input, OnInit} from '@angular/core';
import {RouterLinkWithHref} from '@angular/router';

@Directive({
  selector: 'a[appRouterLink],area[appRouterLink]'
})
export class DynamicRouterLinkDirective extends RouterLinkWithHref implements OnInit {

  @Input() appRouterLink: Array<string>;

  ngOnInit(): void {
    this.routerLink = this.appRouterLink;
    this.href = this.appRouterLink.join('/');
  }


  onClick(button: number, ctrlKey: boolean, shiftKey: boolean, altKey: boolean, metaKey: boolean): boolean {
    this.routerLink = this.appRouterLink;
    (this as any).router.navigateByUrl('/', {skipLocationChange: true});
    return super.onClick(button, ctrlKey, shiftKey, altKey, metaKey);
  }
}

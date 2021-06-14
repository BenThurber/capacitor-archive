import {Directive, Input} from '@angular/core';
import {RouterLink} from '@angular/router';

@Directive({
  selector: '[appRouterLink]'
})
export class DynamicRouterLinkDirective extends RouterLink {

  @Input() appRouterLink: Array<string>;

  onClick() {
    (this as any).routerLink = this.appRouterLink;
    (this as any).router.navigateByUrl('/', {skipLocationChange: true});
    return super.onClick();
  }
}

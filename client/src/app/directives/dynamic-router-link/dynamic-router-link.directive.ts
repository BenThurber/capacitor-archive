import {Directive, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterLinkWithHref} from '@angular/router';
import {LocationStrategy} from '@angular/common';

@Directive({
  selector: 'a[appRouterLink],area[appRouterLink]'
})
export class DynamicRouterLinkDirective extends RouterLinkWithHref implements OnInit {

  @Input() appRouterLink: Array<string>;

  constructor(private router2: Router, private route2: ActivatedRoute, private locationStrategy2: LocationStrategy) {
    super(router2, route2, locationStrategy2);
  }

  ngOnInit(): void {
    this.routerLink = this.appRouterLink;
    this.href = this.appRouterLink.join('/');
  }

  /**
   * Same implementation as RouterLinkWithHref but route goes to '/' then to the destination.
   */
  onClick(button, ctrlKey, shiftKey, altKey, metaKey): boolean {
    if (button !== 0 || ctrlKey || shiftKey || altKey || metaKey) {
      return true;
    }
    if (typeof this.target === 'string' && this.target != '_self') {
      return true;
    }
    const extras = {
      skipLocationChange: attrBoolValue(this.skipLocationChange),
      replaceUrl: attrBoolValue(this.replaceUrl),
      state: this.state
    };
    this.router2.navigateByUrl('/', {skipLocationChange: true}).then(
      () => this.router2.navigateByUrl(this.urlTree, extras)
    );
    return false;
  }
}


function attrBoolValue(s): any {
  return s === '' || !!s;
}

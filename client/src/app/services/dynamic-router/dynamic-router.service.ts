import { Injectable } from '@angular/core';
import {Router} from '@angular/router';

/**
 * This component is a wrapper for @angular/router.  This service overcomes the problem of views
 * not rendering when navigated to from URIs with variables in the path like '/manufacturer/view/hunts'
 */
@Injectable({
  providedIn: 'root'
})
export class DynamicRouterService {

  router: Router;

  constructor(router: Router) {
    this.router = router;
  }

  /**
   * Used to avaoid a bug when navigating to URIs that have a dynamic path.
   * Navigates to a dummy route, then to the desired route.
   * Without using this function, the router-outlet component doesn't refresh when using dynamic URIs.
   * @param uri the location to route to
   */
  redirectTo(uri: Array<string>): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
      this.router.navigate(uri));
  }
}

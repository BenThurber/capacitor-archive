import { Injectable } from '@angular/core';
import {NavigationExtras, Router, UrlTree} from '@angular/router';

/**
 * This component is a wrapper for @angular/router.  This service overcomes the problem of views
 * not rendering when navigated to from URIs with variables in the path like '/manufacturer/view/hunts'
 */
@Injectable({
  providedIn: 'root'
})
export class DynamicRouterService {

  readonly UPPER_CASE_PATHS = [

    /^[0-9]+C[0-9]+V$/    // CapacitorUnit value

  ];

  router: Router;

  constructor(router: Router) {
    this.router = router;
    this.toLowerCaseIfNotMatch = this.toLowerCaseIfNotMatch.bind(this);   // Strange TS behavior requires this
  }

  /**
   * Used to avoid a bug when navigating to URIs that have a dynamic path.
   * Navigates to a dummy route, then to the desired route.
   * Without using this function, the router-outlet component doesn't refresh when using dynamic URIs.
   *
   * Makes route params lower case, unless they match a regular expression defined at the top of this file.
   *
   * @param commands An array of URL fragments with which to construct the target URL.
   * If the path is static, can be the literal URL string. For a dynamic path, pass an array of path
   * segments, followed by the parameters for each segment.
   * The fragments are applied to the current URL or the one provided  in the `relativeTo` property
   * of the options object, if supplied.
   * @param extras An options object that determines how the URL should be constructed or
   *     interpreted.
   */
  async navigate(commands: any[], extras?: NavigationExtras): Promise<boolean> {
    await this.router.navigateByUrl('/', {skipLocationChange: true});
    return this.router.navigate(commands.map(this.toLowerCaseIfNotMatch), extras);
  }

  /**
   * Used to avoid a bug when navigating to URIs that have a dynamic path.
   * Navigates to a dummy route, then to the desired route.
   * Without using this function, the router-outlet component doesn't refresh when using dynamic URIs.
   * @param url An absolute path for a defined route. The function does not apply any delta to the
   * current URL.
   * The fragments are applied to the current URL or the one provided  in the `relativeTo` property
   * of the options object, if supplied.
   * @param extras An options object that determines how the URL should be constructed or
   *     interpreted.
   */
  async navigateByUrl(url: string | UrlTree, extras?: NavigationExtras): Promise<boolean> {
    await this.router.navigateByUrl('/', {skipLocationChange: true});
    return this.router.navigateByUrl(url, extras);
  }


  /**
   * Calls toLowerCase() on string unless it matches a regex in UPPER_CASE_PATHS.
   * @param param an unencoded url path parameter
   */
  toLowerCaseIfNotMatch(param: string): string {
    if (this.UPPER_CASE_PATHS.some(regex => regex.test(param))) {
      return param;
    } else {
      return param.toLowerCase();
    }
  }
}

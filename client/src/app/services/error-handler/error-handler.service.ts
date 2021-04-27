import { Injectable } from '@angular/core';
import {SpringErrorResponse} from '../../models/spring-error-response.model';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(private router: Router) { }

  /**
   * If error is 404, navigate to PageNotFoundComponent, else do console error
   * @param err a SpringErrorResponse from the backend API
   * @param message an optional message to display in console error.  If none is provided, it uses the
   * error message in SpringErrorResponse
   */
  handleGetRequestError(err: SpringErrorResponse, message?: string): void {
    if (err.status === 404) {
      this.router.navigateByUrl('/not-found', {skipLocationChange: true});
      // skipLocationChange keeps the current url in the address bar
    } else {
      console.error(message || err.message || err.error.message);
    }
  }
}

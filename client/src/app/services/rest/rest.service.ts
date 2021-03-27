import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Manufacturer} from '../../models/manufacturer.model';
import {Observable} from 'rxjs';
import {GoogleCaptchaAPIResponse} from '../../models/recaptcha.model';
import {FormGroup} from '@angular/forms';
import {ReCaptcha2Component} from '@niteshp/ngx-captcha';
import {CapacitorType} from '../../models/capacitor-type.model';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {map} from 'rxjs/operators';
import urlcat from 'urlcat';


@Injectable({
  providedIn: 'root'
})
export class RestService {


  baseUrl: string = environment.serverBaseUrl;
  url: string;
  options: object;


  httpClient: HttpClient;

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;

    const httpHeaders = new HttpHeaders({
      'Access-Control-Allow-Origin': this.baseUrl,
      'Content-Type': 'application/json; charset=utf-8',
    });

    this.options = {headers: httpHeaders};
  }

  getManufacturerByName(companyName: string): Observable<Manufacturer> {
    const url = urlcat(this.baseUrl, '/manufacturer/name', {companyName});
    return this.httpClient.get<Manufacturer>(url, this.options);
  }

  getAllCompanyNames(): Observable<Array<string>> {
    return this.httpClient.get<Array<string>>(this.baseUrl + '/manufacturer/all-names', this.options);
  }

  getAllTypes(companyName): Observable<Array<CapacitorType>> {
    const url = urlcat(this.baseUrl, '/type/all', {companyName});
    return this.httpClient.get<Array<CapacitorType>>(url, this.options);
  }

  getAllConstructions(): Observable<Array<string>> {
    return this.httpClient.get<Array<string>>(this.baseUrl + '/construction/all', this.options);
  }

  createManufacturer(manufacturer: Manufacturer): any {
    return this.httpClient.post<any>(this.baseUrl + '/manufacturer/create', manufacturer, this.options);
  }

  createCapacitorType(capacitorType: CapacitorType): any {
    return this.httpClient.post<any>(this.baseUrl + '/type/create', capacitorType, this.options);
  }

  editCapacitorType(companyName: string, typeName: string, capacitorType: CapacitorType): any {
    const url = urlcat(this.baseUrl, '/type/edit', {companyName, typeName});
    return this.httpClient.put<any>(url, capacitorType, this.options);
  }

  editCapacitorUnit(companyName: string, typeName: string, value: string, capacitorUnit: CapacitorUnit): any {
    const url = urlcat(this.baseUrl, '/unit/edit', {companyName, typeName, value});
    return this.httpClient.put<any>(url, capacitorUnit, this.options);
  }

  createConstruction(construction: string): any {
    return this.httpClient.post<any>(this.baseUrl + '/construction/create', construction, {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': this.baseUrl,
        Accept: 'text/plain',
        'Content-Type': 'text/plain'}),
      responseType: 'text' as 'json'     // (This "as json" is to suppress TS warnings)
    });
  }

  createCapacitorUnit(capacitorUnit: CapacitorUnit): any {
    return this.httpClient.post<any>(this.baseUrl + '/unit/create', capacitorUnit, this.options);
  }

  getCapacitorTypeByName(companyName: string, typeName: string): Observable<CapacitorType> {
    const url = urlcat(this.baseUrl, '/type/name', {companyName, typeName});
    return this.httpClient.get<any>(url, this.options);
  }

  getCapacitorUnitByValue(companyName: string, typeName: string, value: string): Observable<CapacitorUnit> {
    const url = urlcat(this.baseUrl, '/unit/name', {companyName, typeName, value});
    return this.httpClient.get<any>(url, this.options).pipe(
      map(cu => new CapacitorUnit(cu)));
  }

  getAllCapacitorUnitsFromCapacitorType(companyName: string, typeName: string): Observable<Array<CapacitorUnit>> {
    const url = urlcat(this.baseUrl, '/unit/all', {companyName, typeName});
    return this.httpClient.get<any>(url, this.options).pipe(
      map(res => res.map(cu => new CapacitorUnit(cu))));
  }

  editManufacturer(companyName: string, manufacturer: Manufacturer): any {
    const url = urlcat(this.baseUrl, '/manufacturer/edit', {companyName});
    return this.httpClient.put<any>(url, manufacturer, this.options);
  }

  verifyCaptcha(captchaTokenResponse: string): any {
    return this.httpClient.post<HttpResponse<GoogleCaptchaAPIResponse>>(
      this.baseUrl + '/captcha/verify', captchaTokenResponse, this.options);
  }







  /**
   * This function is called by a ngx-recaptcha2 component in a (success) event.  It automatically adds errors to the formGroup depending
   * on the result of the verification request to the back-end.  Assumes that there is formControlName="captcha" set in the ngx-recaptcha2
   * component.
   * @param captchaToken set to $event to capture event from component
   * @param formGroup FormGroup containing the ngx-recaptcha2 component.  Must have formControlName captcha.
   * @param captchaElem the html element retrieved with @ViewChild
   */
  handleCaptchaSuccess(captchaToken: string, formGroup: FormGroup, captchaElem: ReCaptcha2Component): void {

    return this.verifyCaptcha(captchaToken).subscribe({
      next: (response: GoogleCaptchaAPIResponse) => {

        if (formGroup.controls.captcha.hasError('noResponse')) {
          delete formGroup.controls.captcha.errors.noResponse;
        }
        if (formGroup.controls.captcha.hasError('rejectedCaptcha')) {
          delete formGroup.controls.captcha.errors.rejectedCaptcha;
        }

        const existingErrors = formGroup.controls.captcha.errors == null ? {} : formGroup.controls.captcha.errors;

        if (!response.success) {
          const errors = {...existingErrors, rejectedCaptcha: true};   // Set rejectedCaptcha error
          captchaElem.resetCaptcha();
          formGroup.controls.captcha.setErrors({...errors, ...formGroup.controls.captcha.errors});
        }

        if (formGroup.controls.captcha.errors === {}) {
          formGroup.controls.captcha.setErrors(null);
        }
      },
      error: () => {

        const existingErrors = formGroup.controls.captcha.errors == null ? {} : formGroup.controls.captcha.errors;

        const errors = {...existingErrors, noResponse: true};   // Set noResponse error
        captchaElem.resetCaptcha();
        formGroup.controls.captcha.setErrors({...errors, ...formGroup.controls.captcha.errors});

      },
    });

  }
}

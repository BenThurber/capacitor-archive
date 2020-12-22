import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Manufacturer} from '../../models/manufacturer.model';
import {Observable} from 'rxjs';
import {GoogleCaptchaAPIResponse} from '../../models/recaptcha.model';

@Injectable({
  providedIn: 'root'
})
export class RestService {


  baseUrl: string = environment.serverBaseUrl;
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

  getManufacturerByName(name: string): Observable<Manufacturer> {
    return this.httpClient.get<Manufacturer>(this.baseUrl + '/manufacturer/name/' + name, this.options);
  }

  getAllCompanyNames(): Observable<Array<string>> {
    return this.httpClient.get<Array<string>>(this.baseUrl + '/manufacturer/all-names', this.options);
  }

  createManufacturer(manufacturer: Manufacturer): any {
    return this.httpClient.post<any>(this.baseUrl + '/manufacturer/create', manufacturer, this.options);
  }

  editManufacturer(name: string, manufacturer: Manufacturer): any {
    return this.httpClient.put<any>(this.baseUrl + '/manufacturer/edit/' + name, manufacturer, this.options);
  }

  verifyCaptcha(captchaTokenResponse: string): any {
    return this.httpClient.post<HttpResponse<GoogleCaptchaAPIResponse>>(
      this.baseUrl + '/captcha/verify', captchaTokenResponse, this.options);
  }
}

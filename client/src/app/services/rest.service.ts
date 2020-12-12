import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Manufacturer} from '../models/manufacturer.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  // **This needs to be an environment variable**
  baseUrl: string = environment.serverBaseUrl;

  constructor(private httpClient: HttpClient) { }

  getManufacturerByName(name: string): Observable<Manufacturer> {
    return this.httpClient.get<Manufacturer>(this.baseUrl + '/manufacturer/name/' + name);
  }
}

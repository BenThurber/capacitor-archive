import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {runTempPackageBin} from '@angular/cli/tasks/install-package';
import {Manufacturer} from '../models/manufacturer.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  // **This needs to be an environment variable**
  baseUrl = 'http://localhost:35505';

  constructor(private httpClient: HttpClient) { }

  getManufacturerById(id: number): Observable<Manufacturer> {
    return this.httpClient.get<Manufacturer>(this.baseUrl + '/manufacturer/id/' + id);
  }
}

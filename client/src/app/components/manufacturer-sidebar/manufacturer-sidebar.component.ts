import { Component, OnInit } from '@angular/core';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-manufacturer-sidebar',
  templateUrl: './manufacturer-sidebar.component.html',
  styleUrls: ['./manufacturer-sidebar.component.css']
})
export class ManufacturerSidebarComponent implements OnInit {

  companyName: string;
  manufacturers$: Array<string>;

  restService: RestService;

  constructor(restService: RestService, activatedRoute: ActivatedRoute) {
    this.restService = restService;
    this.companyName = activatedRoute.snapshot.paramMap.get('companyName');
  }

  ngOnInit(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: manufacturers => {
        manufacturers.sort(this.caseInsensitiveCompare);
        this.manufacturers$ = manufacturers;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }


  caseInsensitiveCompare(a, b): number {
    const nameA = a.toUpperCase(); // ignore upper and lowercase
    const nameB = b.toUpperCase(); // ignore upper and lowercase
    if (nameA < nameB) {
      return -1;
    }
    if (nameA > nameB) {
      return 1;
    }
    // names must be equal
    return 0;
  }

}

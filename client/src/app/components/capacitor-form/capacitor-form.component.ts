import { Component, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-capacitor-form',
  templateUrl: './capacitor-form.component.html',
  styleUrls: ['./capacitor-form.component.css', '../../styles/animations.css']
})
export class CapacitorFormComponent implements OnInit {

  // Manufacturer Section
  readonly newManufacturerOption = '+ Add Manufacturer';
  isNavigatingToCreateManufacturer = false;
  manufacturers$: Array<string>;
  selectedManufacturer: {index: number, value: string};


  constructor(private restService: RestService, private router: Router) { }

  ngOnInit(): void {
    this.getManufacturerList();
  }

  getManufacturerList(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: manufacturers => {
        manufacturers.sort(caseInsensitiveCompare);
        this.manufacturers$ = manufacturers;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }

  manufacturerDropdownChanged(event): void {

    if (event.target.value === this.newManufacturerOption) {

      this.isNavigatingToCreateManufacturer = true;

      setTimeout(() => {
        this.router.navigate(['manufacturer', 'create']).catch(
          () => this.isNavigatingToCreateManufacturer = false
        );

      }, 800);

      this.selectedManufacturer.index = Number.isInteger(event.target.value) ? event.target.value : null;
    }

  }

}

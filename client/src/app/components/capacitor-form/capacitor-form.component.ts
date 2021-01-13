import { Component, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-capacitor-form',
  templateUrl: './capacitor-form.component.html',
  styleUrls: ['./capacitor-form.component.css', '../../styles/animations.css']
})
export class CapacitorFormComponent implements OnInit {

  capacitorForm: FormGroup;

  // Manufacturer Section
  readonly newManufacturerOption = '+ Add Manufacturer';
  isNavigatingToCreateManufacturer = false;
  manufacturers$: Array<string>;


  constructor(private restService: RestService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.getManufacturerList();

    const integerPattern: RegExp = /^\d+$/;
    this.capacitorForm = this.formBuilder.group({
      companyName: ['', Validators.required],
    });
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

    }

  }

  get formFields(): any {
    return this.capacitorForm.controls;
  }

  get manufacturerIsSelected(): any {
    // Inefficient O(n)
    return this.manufacturers$.includes(this.formFields.companyName.value);
  }

}

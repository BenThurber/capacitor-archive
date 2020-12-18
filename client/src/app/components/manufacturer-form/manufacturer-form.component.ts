import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {Location} from '@angular/common';


@Component({
  selector: 'app-manufacturer-form',
  templateUrl: './manufacturer-form.component.html',
  styleUrls: ['./manufacturer-form.component.css']
})
export class ManufacturerFormComponent implements OnInit {

  editMode: boolean;

  manufacturerForm: FormGroup;
  formBuilder: FormBuilder;
  restService: RestService;
  location: Location;

  constructor(formBuilder: FormBuilder, restService: RestService, location: Location) {
    this.formBuilder = formBuilder;
    this.restService = restService;
    this.location = location;
  }


  ngOnInit(): void {
    const integerPattern: RegExp = /^\d+$/;
    this.manufacturerForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      openYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      closeYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      summary: ['', []],
    }, { validator: checkIfCloseYearAfterOpenYear });

    this.editMode = false;  // Temporary
  }

  onSubmit(manufacturerData): void {

    if (this.editMode) {
      // Edit end point goes here

    } else {

      const manufacturer = new Manufacturer();
      manufacturer.insertData(manufacturerData);

      return this.restService.createManufacturer(manufacturer).subscribe({
        next: () => this.location.back(),
        error: error => console.error(error),  // This should be improved
      });
    }


  }

  get formFields(): any {
    return this.manufacturerForm.controls;
  }

  get closeYearAfterOpenYearError(): any {
    return this.manufacturerForm.errors && this.manufacturerForm.errors.closeYearAfterOpenYear;
  }

}


function checkIfCloseYearAfterOpenYear(c: AbstractControl): any {
  // Safety Check
  const openDate: number = parseInt(c.value.openYear, 10);
  const closedDate: number  = parseInt(c.value.closeYear, 10);

  if (!openDate || !closedDate) { return null; }

  return (openDate <= closedDate) ? null : { closeYearAfterOpenYear: true };
  // carry out the actual date checks here for is-endDate-after-startDate
  // if valid, return null,
  // if invalid, return an error object (any arbitrary name), like, return { invalidEndDate: true }
  // make sure it always returns a 'null' for valid or non-relevant cases, and a 'non-null' object for when an error should be raised on
  // the formGroup
}

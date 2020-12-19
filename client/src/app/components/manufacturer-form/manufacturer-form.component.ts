import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {RefreshManufacturersService} from '../../services/refresh-manufacturers/refresh-manufacturers.service';


@Component({
  selector: 'app-manufacturer-form',
  templateUrl: './manufacturer-form.component.html',
  styleUrls: ['./manufacturer-form.component.css']
})
export class ManufacturerFormComponent implements OnInit {

  @Input() manufacturer: Manufacturer;

  manufacturerForm: FormGroup;
  formBuilder: FormBuilder;
  restService: RestService;
  router: Router;
  location: Location;
  refreshManufacturers: RefreshManufacturersService;

  constructor(formBuilder: FormBuilder, restService: RestService, router: Router, location: Location,
              refreshManufacturers: RefreshManufacturersService) {
    this.formBuilder = formBuilder;
    this.restService = restService;
    this.router = router;
    this.location = location;
    this.refreshManufacturers = refreshManufacturers;
  }


  ngOnInit(): void {
    const integerPattern: RegExp = /^\d+$/;
    this.manufacturerForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      openYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      closeYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      summary: ['', []],
    }, { validator: checkIfCloseYearAfterOpenYear });

  }

  onSubmit(manufacturerData): void {

    if (this.manufacturer === undefined) {

      this.submitCreate(manufacturerData);

    } else {

      if (!(this.manufacturer instanceof Object && this.manufacturer.id >= 1)) {
        console.error('Can\'t determine type of manufacturer to edit');
        return;
      }

      this.submitEdit(manufacturerData);

    }

  }



  submitCreate(manufacturerData): void {
    const manufacturer = new Manufacturer();
    manufacturer.insertData(manufacturerData);

    return this.restService.createManufacturer(manufacturer).subscribe({
      next: () => this.router.navigate(['manufacturer', 'view', manufacturer.companyName.toLowerCase()]).then(
        () => this.refreshManufacturers.refresh()
      ),
      error: error => console.error(error),  // This should be improved
    });
  }



  submitEdit(manufacturerData): void {
    const manufacturer = new Manufacturer();
    manufacturer.id = this.manufacturer.id;
    manufacturer.insertData(manufacturerData);

    return this.restService.editManufacturer(this.manufacturer.companyName, manufacturer).subscribe({
      next: () => this.router.navigate(['manufacturer', 'view', manufacturer.companyName.toLowerCase()]).then(
        () => this.refreshManufacturers.refresh()
      ),
      error: error => console.error(error),  // This should be improved
    });
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

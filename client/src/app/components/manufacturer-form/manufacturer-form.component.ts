import {Component, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Manufacturer} from '../../models/manufacturer.model';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {RefreshManufacturersService} from '../../services/refresh-manufacturers/refresh-manufacturers.service';
import {environment} from '../../../environments/environment';
import {ReCaptcha2Component} from '@niteshp/ngx-captcha';
import {SpringErrorResponse} from '../../models/spring-error-response.model';
import {historicCountryList} from '../../utilities/countries';


@Component({
  selector: 'app-manufacturer-form',
  templateUrl: './manufacturer-form.component.html',
  styleUrls: ['./manufacturer-form.component.css', '../../styles/animations.css']
})
export class ManufacturerFormComponent implements OnInit, OnChanges {

  @Input('manufacturer') existingManufacturer: Manufacturer;
  @ViewChild('captchaElem') captchaElem: ReCaptcha2Component;

  submitting = false;
  showCustomCountryNameInput = false;
  errorsBackend: Array<SpringErrorResponse> = [];

  reCaptchaSiteKey = environment.reCaptchaSiteKey;

  currentImageUploads = new Set<string>();

  countries = [...historicCountryList];

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
      country: ['', []],
      openYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      closeYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      summary: ['', []],
      captcha: ['', Validators.required],
    }, {validator: checkIfCloseYearBeforeOpenYear});

    // Populate form values
    if (this.existingManufacturer) {
      // Is country a non-null string that is not in the countries list?
      if (this.existingManufacturer.country && !this.countries.includes(this.existingManufacturer.country)) {
        this.countries.push(this.existingManufacturer.country);
        this.countries.sort();
      }
      this.manufacturerForm.setValue({
        companyName: this.existingManufacturer.companyName,
        country: this.existingManufacturer.country,
        openYear: this.existingManufacturer.openYear,
        closeYear: this.existingManufacturer.closeYear,
        summary: this.existingManufacturer.summary,
        captcha: null,
      });
    }

  }


  /**
   * Initialize existingManufacturer when its value is returned from back end
   */
  ngOnChanges(changes): void {
    // existingManufacturer isn't initialized because its value is from an async function
    this.existingManufacturer = changes.existingManufacturer.currentValue;
    this.ngOnInit();
  }


  onSubmit(manufacturer: Manufacturer): void {
    this.submitting = true;
    this.errorsBackend = [];

    if (this.existingManufacturer === undefined) {

      this.submitCreate(manufacturer);

    } else {

      if (!(this.existingManufacturer instanceof Object && this.existingManufacturer.companyName)) {
        console.error('Can\'t edit manufacturer.  Bad data from backend');
        return;
      }

      this.submitEdit(manufacturer);

    }

  }


  submitCreate(manufacturer: Manufacturer): void {

    return this.restService.createManufacturer(manufacturer).subscribe({
      next: () => this.router.navigate(['manufacturer', 'view', manufacturer.companyName.toLowerCase()]).then(
        () => this.refreshManufacturers.refresh()
      ),
      error: error => this.handleBackendError(error.error),
    });
  }


  submitEdit(manufacturer: Manufacturer): void {

    return this.restService.editManufacturer(this.existingManufacturer.companyName, manufacturer).subscribe({
      next: () => this.router.navigate(['manufacturer', 'view', manufacturer.companyName.toLowerCase()]).then(
        () => this.refreshManufacturers.refresh()
      ),
      error: error => this.handleBackendError(error.error),
    });
  }


  handleBackendError(error: SpringErrorResponse): void {
    this.submitting = false;
    this.errorsBackend.push(error);
  }


  get formFields(): any {
    return this.manufacturerForm.controls;
  }

  get closeYearBeforeOpenYearError(): any {
    return this.manufacturerForm.errors && this.manufacturerForm.errors.closeYearBeforeOpenYear;
  }

}


function checkIfCloseYearBeforeOpenYear(c: AbstractControl): any {

  const openYear: number = parseInt(c.value.openYear, 10);
  const closeYear: number = parseInt(c.value.closeYear, 10);

  if (!openYear || !closeYear) { return null; }

  return (openYear <= closeYear) ? null : { closeYearBeforeOpenYear: true };
  // carry out the actual date checks here for is-endDate-after-startDate
  // if valid, return null,
  // if invalid, return an error object (any arbitrary name), like, return { invalidEndDate: true }
  // make sure it always returns a 'null' for valid or non-relevant cases, and a 'non-null' object for when an error should be raised on
  // the formGroup
}

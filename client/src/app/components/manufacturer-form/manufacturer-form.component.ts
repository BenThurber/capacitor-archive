import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';


@Component({
  selector: 'app-manufacturer-form',
  templateUrl: './manufacturer-form.component.html',
  styleUrls: ['./manufacturer-form.component.css']
})
export class ManufacturerFormComponent implements OnInit {

  manufacturerForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    const integerPattern: RegExp = /^\d+$/;
    this.manufacturerForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      openYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      closeYear: ['', [Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]],
      summary: ['', []],
    }, { validator: checkIfCloseYearAfterOpenYear });
  }


  ngOnInit(): void {
  }

  onSubmit(manufacturerData): void {
    // this.manufacturerForm.reset();

    console.warn('Your form has been submitted', manufacturerData);
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

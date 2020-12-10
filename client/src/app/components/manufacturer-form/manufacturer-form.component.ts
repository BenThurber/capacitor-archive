import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


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
    });
  }

  ngOnInit(): void {
  }

  onSubmit(manufacturerForm: FormGroup): void {
    // Process checkout data here
    manufacturerForm.reset();

    console.warn('Your form has been submitted', manufacturerForm.value);
  }

  public get formControls(): any {
    return this.manufacturerForm.controls;
  }


}

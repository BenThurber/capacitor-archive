import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {INT_TYPE} from '@angular/compiler/src/output/output_ast';

@Component({
  selector: 'app-manufacturer-form',
  templateUrl: './manufacturer-form.component.html',
  styleUrls: ['./manufacturer-form.component.css']
})
export class ManufacturerFormComponent implements OnInit {

  manufacturerForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.manufacturerForm = this.formBuilder.group({
      companyName: '',
      openYear: Number,
      closeYear: Number,
      summary: '',
    });
  }

  ngOnInit(): void {
  }

  onSubmit(customerData): void {
    // Process checkout data here
    this.manufacturerForm.reset();

    console.warn('Your order has been submitted', customerData);
  }

}

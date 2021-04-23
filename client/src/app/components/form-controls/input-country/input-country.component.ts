import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import {historicCountryList} from '../../../utilities/countries';

@Component({
  selector: 'app-input-country',
  templateUrl: './input-country.component.html',
  styleUrls: ['./input-country.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: InputCountryComponent,
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InputCountryComponent implements ControlValueAccessor, OnInit {

  selectValue: string;
  inputValue: string;

  useCustomCountryName = false;
  countries = historicCountryList;

  onChange = event => {};
  onTouched = () => {};

  constructor(private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
  }


  // ------ControlValueAccessor implementations------

  writeValue(country: string): void {

    if (country && !this.countries.includes(country)) {
      this.countries.push(country);
      this.countries.sort();
    }

    this.selectValue = country;
    if (this.changeDetectorRef) {
      this.changeDetectorRef.markForCheck();
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

}

import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-input-capacitance',
  templateUrl: './input-capacitance.component.html',
  styleUrls: ['./input-capacitance.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: InputCapacitanceComponent,
      multi: true
    }
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class InputCapacitanceComponent implements ControlValueAccessor, OnInit {

  @Input('capacitance') inputCapacitance: string;
  capacitance: number;

  readonly unitOptions = {microFarad: 'micro-farad', nanoFarad: 'nano-farad', picoFarad: 'pico-farad'};
  readonly unitArray: Array<string> = Object.values(this.unitOptions);
  selectedUnit: string;

  onChange = event => {};
  onTouched = () => {};

  constructor(private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.capacitance = parseInt(this.inputCapacitance, 10);
  }


  changeMade(capacitance: any): void {
    capacitance = parseFloat(capacitance);

    switch (this.selectedUnit) {
      case this.unitOptions.picoFarad:
        capacitance *= 1;
        break;
      case this.unitOptions.nanoFarad:
        capacitance *= 1000;
        break;
      case this.unitOptions.microFarad:
        capacitance *= 1000000;
        break;
    }
    capacitance = Math.max(capacitance, 1);
    capacitance = isNaN(capacitance) ? null : capacitance;
    console.log(capacitance);
    this.onChange(capacitance);
  }


  // ------ControlValueAccessor implementations------

  writeValue(capacitance: any): void {

    capacitance = parseInt(capacitance, 10);
    capacitance = isNaN(capacitance) ? null : capacitance;

    if (capacitance == null) {
      this.selectedUnit = this.unitOptions.microFarad;
      this.capacitance = null;
    } else if (capacitance < 1000) {
      this.selectedUnit = this.unitOptions.picoFarad;
      this.capacitance = capacitance;
    } else if (capacitance < 1000000) {
      this.selectedUnit = this.unitOptions.nanoFarad;
      this.capacitance = capacitance / 1000;
    } else if (capacitance < 1000000000000) {
      this.selectedUnit = this.unitOptions.microFarad;
      this.capacitance = capacitance / 1000000;
    }

    this.onChange(this.capacitance);
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

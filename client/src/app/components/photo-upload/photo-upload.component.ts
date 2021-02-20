import { Component, OnInit } from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';

@Component({
  selector: 'app-photo-upload',
  templateUrl: './photo-upload.component.html',
  styleUrls: ['./photo-upload.component.css']
})
export class PhotoUploadComponent implements OnInit, ControlValueAccessor {

  items: Array<number>;

  options: any = {
    swapThreshold: 1.0,
    invertSwap: true,
    animation: 200,
    ghostClass: 'ghost',
    direction: 'horizontal',
    group: {
      name: 'shared',
    }
  };

  constructor() { }

  ngOnInit(): void {
    this.items = [1, 2, 3, 4, 5, 6];
  }


  // ------ControlValueAccessor implementations------

  writeValue(photos: any): void {   // ToDo change the passed variable type
  }

  registerOnChange(fn: any): void {
  }

  registerOnTouched(fn: any): void {
  }

}

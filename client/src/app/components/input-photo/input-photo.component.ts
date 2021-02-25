import {Component, Input, OnInit} from '@angular/core';
import {ControlValueAccessor} from '@angular/forms';
import {Photo} from '../../models/file/photo.model';


@Component({
  selector: 'app-input-photo',
  templateUrl: './input-photo.component.html',
  styleUrls: ['./input-photo.component.css'],
})
export class InputPhotoComponent implements OnInit, ControlValueAccessor {

  @Input() dirPath: Array<string>;

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


  onChange = event => {};
  onTouched = () => {};


  constructor() { }

  ngOnInit(): void {
    this.items = [1, 2, 3, 4, 5, 6];
  }



  // ------ControlValueAccessor implementations------

  writeValue(photos: Array<Photo>): void {
    this.onChange(photos);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

}

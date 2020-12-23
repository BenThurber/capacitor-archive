import {Component, forwardRef, ViewChild} from '@angular/core';
import {ControlValueAccessor, DefaultValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {QuillEditorComponent} from 'ngx-quill';

@Component({
  selector: 'app-rich-text-input',
  templateUrl: './rich-text-input.component.html',
  styleUrls: ['./rich-text-input.component.css'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => QuillEditorComponent),
      multi: true,
    },
  ],
})
export class RichTextInputComponent implements ControlValueAccessor {
  @ViewChild(DefaultValueAccessor) private valueAccessor: DefaultValueAccessor;

  quillConfig = {

  };

  quillStyles = {
    height: '250px',
    backgroundColor: '#ffff'
  };








  // ------ControlValueAccessor implementations------

  writeValue(obj: any): void {
    this.valueAccessor.writeValue(obj);
  }

  registerOnChange(fn: any): void {
    this.valueAccessor.registerOnChange(fn);
  }

  registerOnTouched(fn: any): void {
    this.valueAccessor.registerOnTouched(fn);
  }

  setDisabledState(isDisabled: boolean): void {
    this.valueAccessor.setDisabledState(isDisabled);
  }

}

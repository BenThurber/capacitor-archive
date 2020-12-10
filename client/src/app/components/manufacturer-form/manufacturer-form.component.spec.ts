import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManufacturerFormComponent } from './manufacturer-form.component';
import {FormGroup, ReactiveFormsModule} from '@angular/forms';

describe('ManufacturerFormComponent', () => {
  let component: ManufacturerFormComponent;
  let form: FormGroup;
  let fixture: ComponentFixture<ManufacturerFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManufacturerFormComponent ],
      imports: [ReactiveFormsModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManufacturerFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render input elements', () => {
    const compiled = fixture.debugElement.nativeElement;
    const companyNameInput = compiled.querySelector('input[id="company-name"]');
    const openYearInput = compiled.querySelector('input[id="open-year"]');
    const closeYearInput = compiled.querySelector('input[id="close-year"]');

    expect(companyNameInput).toBeTruthy();
    expect(openYearInput).toBeTruthy();
    expect(closeYearInput).toBeTruthy();
  });

  describe('validation', () => {

    beforeEach(() => {
      form = component.manufacturerForm;
    });

    it('should be invalid without manufacturer name', () => {
      expect(form.valid).toBeFalsy();
    });

    it('should be valid with only manufacturer name', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      expect(form.valid).toBeTruthy();
    });

    it('should be invalid with bad date format', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      const openYearInput = form.controls.openYear;
      openYearInput.setValue('abcd');
      expect(form.valid).toBeFalsy();
    });

    it('should be invalid with an unrealistic date', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      const openYearInput = form.controls.openYear;
      openYearInput.setValue('999');
      expect(form.valid).toBeFalsy();
    });

    it('should be valid with openYear 1935', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      const openYearInput = form.controls.openYear;
      openYearInput.setValue('1935');
      expect(form.valid).toBeTruthy();
    });

    it('should be invalid with closeYear before openYear', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      const openYearInput = form.controls.openYear;
      openYearInput.setValue('1923');
      const closeYearInput = form.controls.closeYear;
      closeYearInput.setValue('1922');
      expect(form.valid).toBeFalsy();
    });

    it('should be valid with correct companyName, openYear and closeYear', () => {
      const manufacturerNameInput = form.controls.companyName;
      manufacturerNameInput.setValue('Hunts');
      const openYearInput = form.controls.openYear;
      openYearInput.setValue('1923');
      const closeYearInput = form.controls.closeYear;
      closeYearInput.setValue('1946');
      expect(form.valid).toBeTruthy();
    });

  });

});

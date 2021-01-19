import {Component, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CapacitorType} from '../../models/capacitor-type.model';
import {SpringErrorResponse} from '../../models/spring-error-response.model';
import {Location} from '@angular/common';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {environment} from '../../../environments/environment';
import {ReCaptcha2Component} from '@niteshp/ngx-captcha';

class CapacitorForm {
  companyName: string;
  type: {
    typeNameSelect: string;
    typeContent: {
      typeNameInput: string;
      construction: string;
      constructionInput: string;
      startYear: number;
      endYear: number;
      description: string;
    }
  };
  unit: {
    capacitance: number
    voltage: number;
    identifier: string;
    notes: string;
  };
}

@Component({
  selector: 'app-capacitor-form',
  templateUrl: './capacitor-form.component.html',
  styleUrls: ['./capacitor-form.component.css', '../../styles/animations.css']
})
export class CapacitorFormComponent implements OnInit {

  static readonly newConstructionOption = '+ Add Construction';

  capacitorForm: FormGroup;
  existingCapacitorForm: CapacitorForm;
  submitting = false;
  errorsBackend: Array<SpringErrorResponse> = [];

  readonly noneSelected = 'Choose...';

  // Manufacturer Section
  readonly newManufacturerOption = '+ Add Manufacturer';
  selectedCompanyName: string;
  companyNames$: Array<string> = [];
  isNavigatingToCreateManufacturer = false;

  // Type Section
  readonly newCapacitorTypeOption = '+ Add New Type';
  selectedCapacitorType: CapacitorType;
  capacitorTypes$: Array<CapacitorType> = [];
  readonly newConstructionOption = CapacitorFormComponent.newConstructionOption;
  constructionNames$: Array<string> = [];
  yearsAreExpanded = false;

  // Captcha and Submit
  @ViewChild('captchaElem') captchaElem: ReCaptcha2Component;
  readonly reCaptchaSiteKey = environment.reCaptchaSiteKey;


  constructor(public restService: RestService,
              private router: Router,
              private formBuilder: FormBuilder,
              public location: Location) { }

  ngOnInit(): void {
    this.getManufacturerList();
    this.getConstructionList();

    const integerPattern: RegExp = /^\d+$/;
    // True when !== this.noneSelected
    const noneSelectedPattern: RegExp = new RegExp('^(?!.*^' + this.noneSelected + '$)');
    this.capacitorForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      type: this.formBuilder.group({
        typeNameSelect: ['', Validators.required],
        typeContent: this.formBuilder.group({
          typeNameInput: [{value: '', disabled: true}, Validators.required],
          construction: [{value: this.noneSelected, disabled: true}, Validators.pattern(noneSelectedPattern)],
          constructionInput: [{value: '', disabled: true}, []],
          startYear: [{value: '', disabled: true}, [
            Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]
          ],
          endYear: [{value: '', disabled: true}, [
            Validators.pattern(integerPattern), Validators.min(1000), Validators.max(new Date().getFullYear())]
          ],
          description: [{value: '', disabled: true}, []],
        }, {validator: [checkIfEndYearBeforeStartYear, checkNewConstruction]}),
      }),
      unit: this.formBuilder.group({
        capacitance: ['', Validators.required],
        voltage: ['', [Validators.pattern(integerPattern)]],
        identifier: ['', []],
        notes: ['', []],
      }),
      captcha: ['', Validators.required],
    });
  }

  /** Update this.companyNames$ */
  getManufacturerList(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: manufacturers => {
        manufacturers.sort(caseInsensitiveCompare);
        this.companyNames$ = manufacturers;
      },

      error: () => console.error('Couldn\'t get company names')
    });
  }

  /** Update this.capacitorTypes$ */
  getTypeList(companyName: string): Subscription {
    return this.restService.getAllTypes(companyName).subscribe({
      next: types => {
        types.sort((a: CapacitorType, b: CapacitorType) => caseInsensitiveCompare(a.typeName, b.typeName));
        this.capacitorTypes$ = types;
        this.typeMenuChanged({target: {value: this.noneSelected}});
      },

      error: () => console.error('Couldn\'t get capacitor types')
    });
  }

  /** Update this.capacitorTypes$ */
  getConstructionList(): Subscription {
    return this.restService.getAllConstructions().subscribe({
      next: constructionNames => {
        constructionNames.sort(caseInsensitiveCompare);
        this.constructionNames$ = constructionNames;
      },

      error: () => console.error('Couldn\'t get construction names')
    });
  }

  /** Handle the event when a companyName is selected */
  manufacturerMenuChanged(event): void {
    this.selectedCompanyName = event.target.value;
    if (this.selectedCompanyName === this.newManufacturerOption) {

      this.gotoAddNewManufacturer();

    }

    if (this.manufacturerIsSelected) {
      this.getTypeList(this.selectedCompanyName);
    }

  }

  /** Handle the event when a typeName is selected */
  typeMenuChanged(event): void {
    const selectedTypeName = event.target.value;

    // Inefficient O(n)
    this.selectedCapacitorType = this.capacitorTypes$.filter(ct => ct.typeName === selectedTypeName).pop();

    selectedTypeName === this.newCapacitorTypeOption ?
      this.formFields.type.controls.typeContent.enable() :
      this.formFields.type.controls.typeContent.disable();

    this.capacitorForm.patchValue({
      type: {
        typeContent: {
          typeNameInput: this.selectedCapacitorType && this.selectedCapacitorType.typeName,
          construction: this.selectedCapacitorType ? this.selectedCapacitorType.constructionName : this.noneSelected,
          startYear: this.selectedCapacitorType && this.selectedCapacitorType.startYear,
          endYear: this.selectedCapacitorType && this.selectedCapacitorType.endYear,
          description: this.selectedCapacitorType && this.selectedCapacitorType.description,
        }
      }
    });
    this.yearsAreExpanded = Boolean(this.typeFields.startYear.value || this.typeFields.endYear.value);
  }

  /** Called when the button "Add a new manufacturer" is pressed */
  gotoAddNewManufacturer(): void {
    this.isNavigatingToCreateManufacturer = true;
    this.formFields.companyName.disable();

    setTimeout(() => {
      this.router.navigate(['manufacturer', 'create']).catch(
        () => {
          this.isNavigatingToCreateManufacturer = false;
          this.formFields.companyName.enable();
        }
      );
    }, 700);
  }

  /** Called when the button "Add a new type" is pressed */
  gotoAddNewType(): void {
    this.capacitorForm.patchValue({
      type: {
        typeNameSelect: this.newCapacitorTypeOption
      }
    });
    this.typeMenuChanged({target: {value: this.newCapacitorTypeOption}});
  }



  onSubmit(capacitorForm: CapacitorForm): void {
    this.submitting = true;
    this.errorsBackend = [];

    if (this.existingCapacitorForm === undefined) {

      this.submitCreate(capacitorForm);

    } else {

      // ToDo implement submitEdit

    }

  }


  submitCreate(capacitorForm: CapacitorForm): void {

    // Create new type
    if (capacitorForm.type.typeNameSelect === this.newCapacitorTypeOption) {
      const capacitorTypeFields = capacitorForm.type.typeContent;

      // Create new Construction
      if (capacitorTypeFields.construction === this.newConstructionOption) {
        return this.restService.createConstruction(capacitorTypeFields.constructionInput).subscribe({
          next: () => {
            capacitorForm.type.typeContent.construction = capacitorForm.type.typeContent.constructionInput;
            this.submitCreate(capacitorForm);
          },
          error: error => this.handleBackendError(error.error),
        });
      }

      const capacitorType: CapacitorType = new CapacitorType();
      capacitorType.typeName = capacitorTypeFields.typeNameInput;
      capacitorType.startYear = capacitorTypeFields.startYear;
      capacitorType.endYear = capacitorTypeFields.endYear;
      capacitorType.description = capacitorTypeFields.description;
      capacitorType.companyName = capacitorForm.companyName;
      capacitorType.constructionName = capacitorTypeFields.construction === this.newConstructionOption ?
        capacitorTypeFields.constructionInput : capacitorTypeFields.construction;

      return this.restService.createCapacitorType(capacitorType).subscribe({
        next: () => {
          capacitorForm.type.typeNameSelect = capacitorForm.type.typeContent.typeNameInput;
          this.submitCreate(capacitorForm);
        },
        error: error => this.handleBackendError(error.error),
      });

    }

    // Create Unit
    const capacitorUnit: CapacitorUnit = new CapacitorUnit();
    capacitorUnit.capacitance = capacitorForm.unit.capacitance;
    capacitorUnit.voltage = capacitorForm.unit.voltage;
    capacitorUnit.identifier = capacitorForm.unit.identifier;
    capacitorUnit.notes = capacitorForm.unit.notes;
    capacitorUnit.typeName = capacitorForm.type.typeNameSelect;
    capacitorUnit.companyName = capacitorForm.companyName;

    return this.restService.createCapacitorUnit(capacitorUnit).subscribe({
      next: () => {
        console.log('Successfully submitted');
      },
      error: error => this.handleBackendError(error.error),
    });

  }

  handleBackendError(error: SpringErrorResponse): void {
    this.submitting = false;
    this.errorsBackend.push(error);
  }

  get formFields(): any {
    return this.capacitorForm.controls;
  }

  get typeFields(): any {
    return this.formFields.type.controls.typeContent.controls;
  }

  get manufacturerIsSelected(): any {
    return !this.formFields.companyName.invalid;
  }

  get capacitorTypeIsSelected(): any {
    return !this.formFields.type.controls.typeNameSelect.invalid;
  }

  get endYearBeforeStartYearError(): any {
    return this.formFields.type.controls.typeContent.errors && this.formFields.type.controls.typeContent.errors.endYearBeforeStartYear;
  }

  get noNewConstructionEnteredError(): any {
    return this.formFields.type.controls.typeContent.errors && this.formFields.type.controls.typeContent.errors.noNewConstructionEntered;
  }

}

/**
 * If a + Add a Construction has been selected, nothing has been entered, return an error.
 * @param c form control for typeContent
 * @return error object { noNewConstructionEntered: true } or null
 */
function checkNewConstruction(c: AbstractControl): any {

  if (c.value.construction === CapacitorFormComponent.newConstructionOption && !c.value.constructionInput) {
    return { noNewConstructionEntered: true };
  } else {
    return null;
  }
}

function checkIfEndYearBeforeStartYear(c: AbstractControl): any {

  const startYear: number = parseInt(c.value.startYear, 10);
  const endYear: number = parseInt(c.value.endYear, 10);

  if (!startYear || !endYear) { return null; }

  return (startYear <= endYear) ? null : { endYearBeforeStartYear: true };
  // carry out the actual date checks here for is-endDate-after-startDate
  // if valid, return null,
  // if invalid, return an error object (any arbitrary name), like, return { invalidEndDate: true }
  // make sure it always returns a 'null' for valid or non-relevant cases, and a 'non-null' object for when an error should be raised on
  // the formGroup
}

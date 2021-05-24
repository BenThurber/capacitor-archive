import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {RestService} from '../../services/rest/rest.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CapacitorType} from '../../models/capacitor-type.model';
import {SpringErrorResponse} from '../../models/spring-error-response.model';
import {Location} from '@angular/common';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {environment} from '../../../environments/environment';
import {ReCaptcha2Component} from '@niteshp/ngx-captcha';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {Photo} from '../../models/file/photo.model';

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
    notes: string;
    length: string;
    diameter: string;
    mountingHoleDiameter: string;
    thickness: string;
    photos: Array<Photo>;
  };
}

@Component({
  selector: 'app-capacitor-form',
  templateUrl: './capacitor-form.component.html',
  styleUrls: ['./capacitor-form.component.css', '../../styles/animations.css', '../../styles/expansion-panel.css']
})
export class CapacitorFormComponent implements OnInit, AfterViewInit {

  static readonly newConstructionOption = '+ Add Construction';

  editing = false;
  @Input('companyName') editCompanyName: string;
  @Input('capacitorType') editCapacitorType: CapacitorType;
  @Input('capacitorUnit') editCapacitorUnit: CapacitorUnit;

  @Input() only: 'type' | 'unit' | 'photos';

  capacitorFormGroup: FormGroup;
  submitting = false;
  errorsBackend: Array<SpringErrorResponse> = [];

  readonly noneSelected = 'Choose...';

  // Manufacturer Section
  readonly newManufacturerOption = '+ Add Manufacturer';
  companyNames$: Array<string> = [];
  isNavigatingToCreateManufacturer = false;

  // Type Section
  readonly newCapacitorTypeOption = '+ Add New Type';
  selectedCapacitorType: CapacitorType;
  capacitorTypes$: Array<CapacitorType> = [];
  readonly newConstructionOption = CapacitorFormComponent.newConstructionOption;
  constructionNames$: Array<string> = [];
  yearsAreExpanded = false;
  dimensionsAreExpanded = false;
  currentImageUploads = new Set<string>();

  // Captcha and Submit
  @ViewChild('captchaElem') captchaElem: ReCaptcha2Component;
  @ViewChild('submitDiv') submitDiv: ElementRef;
  readonly reCaptchaSiteKey = environment.reCaptchaSiteKey;


  constructor(private activatedRoute: ActivatedRoute,
              public restService: RestService,
              private router: Router,
              private dynamicRouter: DynamicRouterService,
              private formBuilder: FormBuilder,
              public location: Location) { }

  ngOnInit(): void {
    this.getManufacturerList();
    this.getConstructionList();

    const integerPattern: RegExp = /^\d+$/;
    // True when !== this.noneSelected
    const noneSelectedPattern: RegExp = new RegExp('^(?!.*^' + this.noneSelected + '$)');
    this.capacitorFormGroup = this.formBuilder.group({
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
        capacitance: ['', this.only === 'type' ? [] : Validators.required],
        voltage: ['', [Validators.pattern(integerPattern)]],
        notes: ['', []],
        length: ['', []],
        diameter: ['', []],
        mountingHoleDiameter: ['', []],
        thickness: ['', []],
        photos: [new Array<Photo>(), []],
      }),
      captcha: ['', Validators.required],
    });

    // Setup for editing
    if (this.editCompanyName && this.editCapacitorType) {
      this.editing = true;
      this.populateFormFieldsEditing(this.editCompanyName, this.editCapacitorType, this.editCapacitorUnit);
      this.expandMenus();
      this.formFields.type.controls.typeContent.enable();
    }
  }

  ngAfterViewInit(): void {
    if (this.only === 'photos') {
      this.submitDiv.nativeElement.scrollIntoView({behavior: 'auto', block: 'nearest', inline: 'nearest'});
    }
  }

  /**
   * Populates the fields of the FormGroup when editing.  Uses the values from @Input
   */
  private populateFormFieldsEditing(companyName: string, capacitorType: CapacitorType, capacitorUnit: CapacitorUnit): void {
    this.capacitorFormGroup.patchValue({
      companyName,
      type: {
        typeNameSelect: capacitorType.typeName,
        typeContent: {
          typeNameInput: capacitorType.typeName,
          construction: capacitorType.constructionName,
          constructionInput: '',
          startYear: capacitorType.startYear,
          endYear: capacitorType.endYear,
          description: capacitorType.description
        }
      },
    });
    if (this.only !== 'type' && capacitorUnit) {
      this.capacitorFormGroup.patchValue({
        unit: {
          capacitance: capacitorUnit.capacitance,
          voltage: capacitorUnit.voltage,
          notes: capacitorUnit.notes,
          length: capacitorUnit.length,
          diameter: capacitorUnit.diameter,
          mountingHoleDiameter: capacitorUnit.mountingHoleDiameter,
          thickness: capacitorUnit.thickness,
          photos: capacitorUnit.getOrderedPhotos(),
        },
      });
    }
  }

  /**
   * Handle enter keypress inside a form group.  Only submits a form if enter is pressed within an input element.
   * @param event key event
   */
  handleEnterKeyPress(event): boolean {
    const tagName = event.target.tagName.toLowerCase();
    if (tagName === 'input') {
      this.onSubmit(this.capacitorFormGroup);
      return false;
    }
  }

  /** Expand all menus that have data in them, collapse all that don't */
  private expandMenus(): void {
    this.yearsAreExpanded = Boolean(this.typeFields.startYear.value || this.typeFields.endYear.value);
    this.dimensionsAreExpanded = Boolean(
      this.formFields.unit.controls.length.value ||
      this.formFields.unit.controls.diameter.value ||
      this.formFields.unit.controls.mountingHoleDiameter.value ||
      this.formFields.unit.controls.thickness.value
    );
  }

  /** Inserts a manufacturer name into the dropdown menu if it exists in the url */
  private populateManufacturerCreating(allCompanyNames: Array<string>): void {
    if (this.editing) {
      return;
    }

    const lowercaseCompanyName = this.activatedRoute.snapshot.paramMap.get('companyName');

    let companyName;
    allCompanyNames.forEach(name => {
      if (name.toLocaleLowerCase() === lowercaseCompanyName) {
        companyName = name;
      }
    });

    if (companyName) {
      this.capacitorFormGroup.controls.companyName.setValue(companyName);
      this.manufacturerMenuChanged(companyName);
    }
  }

  /** Inserts a type into the dropdown menu if it exists in the url */
  private populateTypeNameCreating(allTypes: Array<CapacitorType>): void {
    if (this.editing) {
      return;
    }

    const lowercaseTypeName = this.activatedRoute.snapshot.paramMap.get('typeName');

    let typeName;
    allTypes.forEach(type => {
      if (type.typeName.toLocaleLowerCase() === lowercaseTypeName) {
        typeName = type.typeName;
      }
    });

    if (typeName) {
      this.formFields.type.controls.typeNameSelect.setValue(typeName);
      this.typeMenuChanged(typeName);
    }
  }

  /** Update this.companyNames$ */
  getManufacturerList(): Subscription {
    return this.restService.getAllCompanyNames().subscribe({
      next: companyNames => {
        companyNames.sort(caseInsensitiveCompare);
        this.companyNames$ = companyNames;
        this.populateManufacturerCreating(this.companyNames$);
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
        this.typeMenuChanged(this.noneSelected);
        this.populateTypeNameCreating(this.capacitorTypes$);
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
  manufacturerMenuChanged(value): void {
    if (value === this.newManufacturerOption) {

      this.gotoAddNewManufacturer();

    }

    if (this.manufacturerIsSelected) {
      this.getTypeList(value);
    }

    this.capacitorFormGroup.patchValue({
      companyName: value
    });

  }

  /** Handle the event when a typeName is selected */
  typeMenuChanged(selectedTypeName): void {

    // Inefficient O(n)
    this.selectedCapacitorType = this.capacitorTypes$.filter(ct => ct.typeName === selectedTypeName).pop();

    selectedTypeName === this.newCapacitorTypeOption ?
      this.formFields.type.controls.typeContent.enable() :
      this.formFields.type.controls.typeContent.disable();

    this.capacitorFormGroup.patchValue({
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
    this.capacitorFormGroup.patchValue({
      type: {
        typeNameSelect: this.newCapacitorTypeOption
      }
    });
    this.typeMenuChanged(this.newCapacitorTypeOption);
  }


  /**
   * Function called when submitting the form.  Calls submitCreateEditRecursive.
   * @param capacitorFormGroup form data
   */
  onSubmit(capacitorFormGroup: FormGroup): void {
    if (capacitorFormGroup.invalid || this.currentImageUploads.size > 0) {
      return;
    }

    this.submitting = true;
    this.errorsBackend = [];

    this.submitCreateEditRecursive(capacitorFormGroup.value);
  }


  /**
   * Used when creating and editing.  Sends one or more http requests to create/edit CapacitorType, Construction, and CapacitorUnit
   * depending on what form controls have been entered.  If multiple need to be created/edited, then this method is called recursively
   * after each successful http request is completed.
   * @param capacitorForm form data
   */
  submitCreateEditRecursive(capacitorForm: CapacitorForm): void {
    let httpRequestObservable;

    // Create new type
    if (!this.formFields.type.controls.typeContent.pristine) {
      const capacitorTypeFields = capacitorForm.type.typeContent;

      // Create new Construction
      if (capacitorTypeFields.construction === this.newConstructionOption) {
        return this.restService.createConstruction(capacitorTypeFields.constructionInput).subscribe({
          next: () => {
            capacitorForm.type.typeContent.construction = capacitorForm.type.typeContent.constructionInput;
            this.submitCreateEditRecursive(capacitorForm);
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

      httpRequestObservable = this.editing ?
        this.restService.editCapacitorType(this.editCompanyName, this.editCapacitorType.typeName, capacitorType) :
        this.restService.createCapacitorType(capacitorType);

      return httpRequestObservable.subscribe({
        next: () => {
          this.formFields.type.controls.typeContent.markAsPristine();
          this.submitCreateEditRecursive(capacitorForm);
        },
        error: error => this.handleBackendError(error.error),
      });

    }

    // Create Unit
    if (!this.formFields.unit.pristine) {

      const capacitorUnit: CapacitorUnit = new CapacitorUnit();
      capacitorUnit.capacitance = capacitorForm.unit.capacitance;
      capacitorUnit.voltage = capacitorForm.unit.voltage;
      capacitorUnit.notes = capacitorForm.unit.notes;
      capacitorUnit.length = capacitorForm.unit.length;
      capacitorUnit.diameter = capacitorForm.unit.diameter;
      capacitorUnit.mountingHoleDiameter = capacitorForm.unit.mountingHoleDiameter;
      capacitorUnit.thickness = capacitorForm.unit.thickness;
      capacitorUnit.setOrderedPhotos(capacitorForm.unit.photos);
      // Remove circular references
      capacitorUnit.photos.forEach(p => p.thumbnails.forEach(t => t.photo = null));
      capacitorUnit.typeName = capacitorForm.type.typeContent ?
        capacitorForm.type.typeContent.typeNameInput : capacitorForm.type.typeNameSelect;
      capacitorUnit.companyName = capacitorForm.companyName;

      httpRequestObservable = this.editing ?
        this.restService.editCapacitorUnit(this.editCompanyName, capacitorUnit.typeName, this.editCapacitorUnit.value,
          capacitorUnit) :
        this.restService.createCapacitorUnit(capacitorUnit);

      return httpRequestObservable.subscribe({
        next: (returnedCapacitorUnit: CapacitorUnit) => {
          this.dynamicRouter.navigate([
            '/capacitor',
            'view',
            returnedCapacitorUnit.companyName,
            returnedCapacitorUnit.typeName,
            returnedCapacitorUnit.value
          ]);
          return;
        },
        error: error => this.handleBackendError(error.error),
      });

    }
    // Only executed if a unit isn't created/edited
    const route = [
      '/capacitor',
      'view',
      (this.editCompanyName || capacitorForm.companyName),
      ((capacitorForm.type.typeContent ? capacitorForm.type.typeContent.typeNameInput : capacitorForm.type.typeNameSelect)
        || this.editCapacitorType.typeName),
    ];
    if (this.editCapacitorUnit) { route.push(this.editCapacitorUnit.value); }
    this.dynamicRouter.navigate(route);

  }



  handleBackendError(error: SpringErrorResponse): void {
    this.submitting = false;
    this.errorsBackend.push(error);
  }

  get formFields(): any {
    return this.capacitorFormGroup.controls;
  }

  get typeFields(): any {
    return this.formFields.type.controls.typeContent.controls;
  }

  get manufacturerIsSelected(): boolean {
    return !this.formFields.companyName.invalid;
  }

  get capacitorTypeIsSelected(): boolean {
    return !this.formFields.type.controls.typeNameSelect.invalid;
  }

  get endYearBeforeStartYearError(): boolean {
    return this.formFields.type.controls.typeContent.errors && this.formFields.type.controls.typeContent.errors.endYearBeforeStartYear;
  }

  get noNewConstructionEnteredError(): boolean {
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

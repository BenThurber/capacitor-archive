import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {caseInsensitiveCompare} from '../../utilities/text-utils';
import {RestService} from '../../services/rest/rest.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CapacitorType} from '../../models/capacitor-type';

@Component({
  selector: 'app-capacitor-form',
  templateUrl: './capacitor-form.component.html',
  styleUrls: ['./capacitor-form.component.css', '../../styles/animations.css']
})
export class CapacitorFormComponent implements OnInit {

  capacitorForm: FormGroup;

  // Manufacturer Section
  readonly newManufacturerOption = '+ Add Manufacturer';
  selectedCompanyName: string;
  companyNames$: Array<string> = [];
  capacitorTypes$: Array<CapacitorType> = [];
  isNavigatingToCreateManufacturer = false;

  // Type Section
  readonly newCapacitorTypeOption = '+ Add New Type';
  selectedCapacitorType: CapacitorType;


  constructor(private restService: RestService,
              private router: Router,
              private formBuilder: FormBuilder,
              private changeDetectorRef: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getManufacturerList();

    const integerPattern: RegExp = /^\d+$/;
    this.capacitorForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      type: this.formBuilder.group({
        typeNameSelect: ['', Validators.required],
        typeNameInput: ['', Validators.required],
        construction: ['', Validators.required],
        startYear: ['', Validators.required],
        endYear: ['', Validators.required],
        description: ['', Validators.required],
      })
    });
    console.log(this.capacitorForm.controls.type.value.typeNameSelect);
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
    return this.restService.getAllTypeNames(companyName).subscribe({
      next: types => {
        types.sort(caseInsensitiveCompare);
        this.capacitorTypes$ = types;
      },

      error: e => console.error('Couldn\'t get capacitor types', e)
    });
  }

  /** Handle the event when a companyName is selected */
  manufacturerMenuChanged(event): void {
    this.selectedCompanyName = event.target.value;
    if (this.selectedCompanyName === this.newManufacturerOption) {

      this.selectAddNewManufacturer();

    }

    if (this.manufacturerIsSelected) {
      this.getTypeList(this.selectedCompanyName);
    }

  }

  /** Handle the event when a typeName is selected */
  typeMenuChanged(event): void {
    // Inefficient O(n)
    this.selectedCapacitorType = this.capacitorTypes$.filter(ct => ct.typeName === event.target.value).pop();
    this.capacitorForm.patchValue({
      type: {
        construction: this.selectedCapacitorType && this.selectedCapacitorType.constructionName,
        startYear: this.selectedCapacitorType && this.selectedCapacitorType.startYear,
        endYear: this.selectedCapacitorType && this.selectedCapacitorType.endYear,
        description: this.selectedCapacitorType && this.selectedCapacitorType.description,
      }
    });
  }

  /** Called when the button "Add a new manufacturer" is pressed */
  selectAddNewManufacturer(): void {
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
  selectAddNewType(): void {
    this.capacitorForm.patchValue({
      type: {
        typeNameSelect: this.newCapacitorTypeOption
      }
    });
    this.typeMenuChanged({target: {value: null}});
  }

  get formFields(): any {
    return this.capacitorForm.controls;
  }

  get manufacturerIsSelected(): any {
    // Inefficient O(n)
    return this.companyNames$.includes(this.formFields.companyName.value);
  }

}

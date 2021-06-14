import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-capacitor-type-panel',
  templateUrl: './capacitor-type-panel.component.html',
  styleUrls: ['./capacitor-type-panel.component.css', '../../styles/animations.css', '../../styles/expansion-panel.css']
})
export class CapacitorTypePanelComponent implements OnInit, OnChanges {

  /**
   * An observable that returns an Array of CapacitorTypes.
   */
  @Input() capacitorTypesObservable: Observable<Array<CapacitorTypeSearchResponse>>;
  @Input() companyName: string;

  capacitorTypes: Array<CapacitorTypeSearchResponse> = [];
  typesMenuIsExpanded = false;
  capacitorTypesLoading = true;
  createNewCapacitorPath: Array<string>;

  constructor(public router: Router) {
  }

  ngOnInit(): void {
    this.capacitorTypesLoading = true;
    this.typesMenuIsExpanded = false;

    this.createNewCapacitorPath = this.companyName ? ['/capacitor', 'create', this.companyName] : ['/capacitor', 'create'];

    this.capacitorTypesObservable.subscribe((capacitorTypes: Array<CapacitorTypeSearchResponse>) => {
      this.capacitorTypes = capacitorTypes;
      this.capacitorTypesLoading = false;
      setTimeout(() => this.typesMenuIsExpanded = true, 100);
    });
  }

  ngOnChanges(changes): void {
    this.capacitorTypesObservable = changes.capacitorTypesObservable.currentValue;
    this.ngOnInit();
  }

}

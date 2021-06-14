import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';

@Component({
  selector: 'app-capacitor-type-panel',
  templateUrl: './capacitor-type-panel.component.html',
  styleUrls: ['./capacitor-type-panel.component.css', '../../styles/animations.css', '../../styles/expansion-panel.css']
})
export class CapacitorTypePanelComponent implements OnInit {

  /**
   * An observable that returns an Array of CapacitorTypes.
   */
  @Input() capacitorTypesObservable: Observable<Array<CapacitorTypeSearchResponse>>;
  @Input() companyName: string;

  capacitorTypes: Array<CapacitorTypeSearchResponse> = [];
  typesMenuIsExpanded = false;
  capacitorTypesLoading = true;
  createNewCapacitorPath: Array<string>;

  constructor() {
  }

  ngOnInit(): void {

    this.createNewCapacitorPath = this.companyName ? ['/capacitor', 'create', this.companyName] : ['/capacitor', 'create'];

    this.capacitorTypesObservable.subscribe((capacitorTypes: Array<CapacitorTypeSearchResponse>) => {
      this.capacitorTypes = capacitorTypes;
      this.capacitorTypesLoading = false;
      setTimeout(() => this.typesMenuIsExpanded = true, 100);
    });
  }

}

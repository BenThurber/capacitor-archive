import {Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
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

  constructor(public dynamicRouter: DynamicRouterService) {
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

import {Component, Input, OnInit} from '@angular/core';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {

  @Input() typeData: CapacitorTypeSearchResponse;
  viewCapacitorPath: Array<string>;

  format = (capacitance: number) => CapacitorUnit.formattedCapacitance(capacitance, true, true);

  constructor(public dynamicRouter: DynamicRouterService) { }

  ngOnInit(): void {
    this.viewCapacitorPath = ['capacitor', 'view', this.typeData.companyName, this.typeData.typeName];
  }

}

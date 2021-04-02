import {Component, Input, OnInit} from '@angular/core';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {

  @Input() typeData: CapacitorTypeSearchResponse;
  viewCapacitorPath: Array<string>;

  constructor(public dynamicRouter: DynamicRouterService) { }

  ngOnInit(): void {
    this.viewCapacitorPath = ['capacitor', 'view', this.typeData.companyName, this.typeData.typeName];
  }

}

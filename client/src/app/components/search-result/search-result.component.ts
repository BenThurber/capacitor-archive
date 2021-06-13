import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {CapacitorTypeSearchResponse} from '../../models/capacitor-type-search-response.model';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {CapacitorUnit} from '../../models/capacitor-unit.model';
import {InputRichTextComponent} from '../form-controls/input-rich-text/input-rich-text.component';


@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit, OnChanges {

  @Input() typeData: CapacitorTypeSearchResponse;
  viewCapacitorPath: Array<string>;
  capacitorTypeDescriptionPlainText = '';

  format = (capacitance: number) => CapacitorUnit.formattedCapacitance(capacitance, true, true);

  constructor(public dynamicRouter: DynamicRouterService) { }

  ngOnInit(): void {
    this.viewCapacitorPath = ['/capacitor', 'view', this.typeData.companyName, this.typeData.typeName];
  }

  ngOnChanges(changes): void {
    this.capacitorTypeDescriptionPlainText = InputRichTextComponent.htmlToText(changes.typeData.currentValue.description);
  }

}

import {Component, Input, OnInit} from '@angular/core';
import {DynamicRouterService} from '../../services/dynamic-router/dynamic-router.service';
import {CapacitorType} from '../../models/capacitor-type.model';

@Component({
  selector: 'app-full-type',
  templateUrl: './full-type.component.html',
  styleUrls: ['./full-type.component.css']
})
export class FullTypeComponent implements OnInit {

  @Input() capacitorType: CapacitorType;
  @Input('capacitorUnitValue') value: string;

  constructor(public dynamicRouter: DynamicRouterService) { }

  ngOnInit(): void {
  }

}

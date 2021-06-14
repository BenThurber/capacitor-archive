import {Component, Input, OnInit} from '@angular/core';
import {CapacitorType} from '../../models/capacitor-type.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-full-type',
  templateUrl: './full-type.component.html',
  styleUrls: ['./full-type.component.css']
})
export class FullTypeComponent implements OnInit {

  @Input() capacitorType: CapacitorType;
  @Input('capacitorUnitValue') value: string;

  constructor(public router: Router) { }

  ngOnInit(): void {
  }

}

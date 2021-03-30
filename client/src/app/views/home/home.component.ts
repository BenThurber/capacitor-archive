import { Component, OnInit } from '@angular/core';
import {ShowSidebarService} from '../../services/show-sidebar/show-sidebar.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  showSidebar: ShowSidebarService;

  constructor(showSidebar: ShowSidebarService) {
    this.showSidebar = showSidebar;
  }

  ngOnInit(): void {
  }

}

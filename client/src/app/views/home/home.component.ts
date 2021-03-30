import { Component, OnInit } from '@angular/core';
import {ShowSidebarService} from '../../services/show-sidebar/show-sidebar.service';
import Timeout = NodeJS.Timeout;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  showSidebarService: ShowSidebarService;

  sidebarMessageShown = null;
  timeoutId: Timeout;

  constructor(showSidebar: ShowSidebarService) {
    this.showSidebarService = showSidebar;
  }

  displayMessage(): void {
    this.sidebarMessageShown = true;
    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.sidebarMessageShown = false, 5000);
  }

  ngOnInit(): void {
  }

}

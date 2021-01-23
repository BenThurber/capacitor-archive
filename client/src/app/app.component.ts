import {Component, OnInit} from '@angular/core';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'Capacitor Archive';

  public constructor(private titleService: Title) {
    // Set the title in the browser window
    this.titleService.setTitle(this.title);
  }


  ngOnInit(): void {
  }

}

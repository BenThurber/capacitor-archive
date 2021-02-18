import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  age: number;

  constructor() { }

  ngOnInit(): void {
    const dob = new Date('February 16, 1997');
    const ageDifMs = Date.now() - dob.getTime();
    const ageDate = new Date(ageDifMs);
    this.age = Math.abs(ageDate.getUTCFullYear() - 1970);
  }

}

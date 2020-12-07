import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-not-implemented',
  template: `
    <h2>Sorry this feature hasn't been created yet.</h2>
    <p>Please check back in a future update</p>
  `,
  styles: [
    '* {text-align: center}'
  ]
})
export class NotImplementedComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}

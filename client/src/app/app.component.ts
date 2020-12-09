import {Component, OnInit} from '@angular/core';
import { Todo } from './todo';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'Capacitor Archive';

  todoValue: string;
  list: Todo[];

  public constructor(private titleService: Title) {
    // Set the title in the browser window
    this.titleService.setTitle(this.title);
  }


  ngOnInit(): void {
    this.list = [];
    this.todoValue = '';
  }

  addItem(): void {
    if (this.todoValue !== '') {
      const newItem: Todo = {
        id: Date.now(),
        value: this.todoValue,
        isDone: false,
      };
      this.list.push(newItem);
    }
    this.todoValue = '';
  }

  deleteItem(id: number): void {
    console.log('Deleted');
    this.list = this.list.filter(item => item.id !== id);
  }
}

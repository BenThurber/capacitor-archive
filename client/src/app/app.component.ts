import {Component, OnInit} from '@angular/core';
import { Todo } from './todo';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  title = 'The Capacitor Archive';

  todoValue: string;
  list: Todo[];

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

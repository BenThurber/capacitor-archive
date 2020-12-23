import { Component } from '@angular/core';

@Component({
  selector: 'app-rich-text-input',
  templateUrl: './rich-text-input.component.html',
  styleUrls: ['./rich-text-input.component.css']
})
export class RichTextInputComponent {

  quillConfig = {

  };

  quillStyles = {
    height: '250px',
    backgroundColor: '#ffff'
  };

}

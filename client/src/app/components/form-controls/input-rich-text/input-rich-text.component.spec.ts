import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputRichTextComponent } from './input-rich-text.component';
import Quill from 'quill';
import ImageResize from 'quill-image-resize-module';

Quill.register('modules/imageResize', ImageResize);

describe('InputRichTextComponent', () => {
  let component: InputRichTextComponent;
  let fixture: ComponentFixture<InputRichTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputRichTextComponent ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputRichTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

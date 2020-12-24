import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RichTextInputComponent } from './rich-text-input.component';
import Quill from 'quill';
import ImageResize from 'quill-image-resize-module';

Quill.register('modules/imageResize', ImageResize);

describe('RichTextInputComponent', () => {
  let component: RichTextInputComponent;
  let fixture: ComponentFixture<RichTextInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RichTextInputComponent ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RichTextInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

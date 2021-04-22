import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputPhotoComponent } from './input-photo.component';

describe('InputPhotoComponent', () => {
  let component: InputPhotoComponent;
  let fixture: ComponentFixture<InputPhotoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputPhotoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputPhotoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

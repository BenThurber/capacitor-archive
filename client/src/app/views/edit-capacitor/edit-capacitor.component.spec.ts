import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCapacitorComponent } from './edit-capacitor.component';

describe('EditCapacitorComponent', () => {
  let component: EditCapacitorComponent;
  let fixture: ComponentFixture<EditCapacitorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCapacitorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCapacitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

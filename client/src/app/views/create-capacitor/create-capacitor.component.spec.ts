import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCapacitorComponent } from './create-capacitor.component';

describe('CreateCapacitorComponent', () => {
  let component: CreateCapacitorComponent;
  let fixture: ComponentFixture<CreateCapacitorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateCapacitorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateCapacitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

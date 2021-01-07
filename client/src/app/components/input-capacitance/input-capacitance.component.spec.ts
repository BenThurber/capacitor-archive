import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputCapacitanceComponent } from './input-capacitance.component';

describe('InputCapacitanceComponent', () => {
  let component: InputCapacitanceComponent;
  let fixture: ComponentFixture<InputCapacitanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputCapacitanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InputCapacitanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

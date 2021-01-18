import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCapacitorComponent } from './view-capacitor.component';

describe('ViewCapacitorComponent', () => {
  let component: ViewCapacitorComponent;
  let fixture: ComponentFixture<ViewCapacitorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewCapacitorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCapacitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

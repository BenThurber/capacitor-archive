import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewManufacturerComponent } from './view-manufacturer.component';

describe('ViewManufacturerComponent', () => {
  let component: ViewManufacturerComponent;
  let fixture: ComponentFixture<ViewManufacturerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewManufacturerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewManufacturerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

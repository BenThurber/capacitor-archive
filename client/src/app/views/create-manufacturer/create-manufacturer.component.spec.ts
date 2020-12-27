import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateManufacturerComponent } from './create-manufacturer.component';

describe('CreateManufacturerComponent', () => {
  let component: CreateManufacturerComponent;
  let fixture: ComponentFixture<CreateManufacturerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateManufacturerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateManufacturerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapacitorFormComponent } from './capacitor-form.component';

describe('CapacitorFormComponent', () => {
  let component: CapacitorFormComponent;
  let fixture: ComponentFixture<CapacitorFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CapacitorFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CapacitorFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

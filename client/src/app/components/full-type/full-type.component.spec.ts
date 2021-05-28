import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FullTypeComponent } from './full-type.component';

describe('ViewFullTypeComponent', () => {
  let component: FullTypeComponent;
  let fixture: ComponentFixture<FullTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FullTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FullTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

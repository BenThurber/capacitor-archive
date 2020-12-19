import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManufacturerSidebarComponent } from './manufacturer-sidebar.component';

describe('ManufacturerSidebarComponent', () => {
  let component: ManufacturerSidebarComponent;
  let fixture: ComponentFixture<ManufacturerSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManufacturerSidebarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManufacturerSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

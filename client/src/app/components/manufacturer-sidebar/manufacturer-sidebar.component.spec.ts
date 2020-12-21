import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManufacturerSidebarComponent } from './manufacturer-sidebar.component';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';

describe('ManufacturerSidebarComponent', () => {
  let component: ManufacturerSidebarComponent;
  let fixture: ComponentFixture<ManufacturerSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManufacturerSidebarComponent ],
      imports: [
        HttpClientModule,
        RouterModule.forRoot([], { relativeLinkResolution: 'legacy' }),
      ],
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

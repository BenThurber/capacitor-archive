import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CapacitorTypePanelComponent } from './capacitor-type-panel.component';

describe('CapacitorTypePanelComponent', () => {
  let component: CapacitorTypePanelComponent;
  let fixture: ComponentFixture<CapacitorTypePanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CapacitorTypePanelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CapacitorTypePanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

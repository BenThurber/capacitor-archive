import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import {NavbarComponent} from './components/nav-bar/navbar.component';
import {Title} from '@angular/platform-browser';

describe('AppComponent', () => {

  let titleService: Title;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        FormsModule
      ],
      declarations: [
        AppComponent,
        NavbarComponent
      ],
      providers: [
        { provide: Title, useClass: Title }],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'client'`, () => {
    titleService = TestBed.get(Title);
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(titleService.getTitle()).toEqual(app.title);
  });

  it('should render header', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('app-navbar')).toBeTruthy();
  });
});

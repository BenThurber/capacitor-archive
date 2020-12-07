import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './nav-bar/navbar.component';
import { BottomBarComponent } from './bottom-bar/bottom-bar.component';
import { NotImplementedComponent } from './not-implemented/not-implemented.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    BottomBarComponent,
    NotImplementedComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/nav-bar/navbar.component';
import { BottomBarComponent } from './components/bottom-bar/bottom-bar.component';
import { NotImplementedComponent } from './views/not-implemented/not-implemented.component';
import { AboutComponent } from './views/about/about.component';
import { HomeComponent } from './views/home/home.component';
import { CreateManufacturerComponent } from './views/create-manufacturer/create-manufacturer.component';
import { ManufacturerFormComponent } from './components/manufacturer-form/manufacturer-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxCaptchaModule } from '@niteshp/ngx-captcha';
import { ViewManufacturerComponent } from './views/view-manufacturer/view-manufacturer.component';
import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';

import {RestService} from './services/rest/rest.service';
import { EditManufacturerComponent } from './views/edit-manufacturer/edit-manufacturer.component';
import {CommonModule} from '@angular/common';
import { ManufacturerSidebarComponent } from './components/manufacturer-sidebar/manufacturer-sidebar.component';
import {QuillEditorComponent, QuillModule} from 'ngx-quill';
import { RichTextInputComponent } from './components/rich-text-input/rich-text-input.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    BottomBarComponent,
    NotImplementedComponent,
    AboutComponent,
    HomeComponent,
    CreateManufacturerComponent,
    ManufacturerFormComponent,
    ViewManufacturerComponent,
    PageNotFoundComponent,
    EditManufacturerComponent,
    ManufacturerSidebarComponent,
    RichTextInputComponent,
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgxCaptchaModule,
    HttpClientModule,
    CommonModule,
    QuillModule.forRoot(),
  ],
  providers: [RestService, QuillEditorComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }

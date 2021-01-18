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
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatTabsModule} from '@angular/material/tabs';
import { SafeHtmlPipe } from './pipes/safe-html.pipe';
import { CreateCapacitorComponent } from './views/create-capacitor/create-capacitor.component';
import { CapacitorFormComponent } from './components/capacitor-form/capacitor-form.component';
import { InputCapacitanceComponent } from './components/input-capacitance/input-capacitance.component';
import {MatExpansionModule} from '@angular/material/expansion';
import { ImageComponent } from './components/image/image.component';
import { ViewCapacitorComponent } from './views/view-capacitor/view-capacitor.component';


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
    SafeHtmlPipe,
    CreateCapacitorComponent,
    CapacitorFormComponent,
    InputCapacitanceComponent,
    ImageComponent,
    ViewCapacitorComponent,
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgxCaptchaModule,
    HttpClientModule,
    CommonModule,
    QuillModule.forRoot({
      suppressGlobalRegisterWarning: true
    }),
    BrowserAnimationsModule,
    MatTabsModule,
    MatExpansionModule,
  ],
  providers: [RestService, QuillEditorComponent],
  bootstrap: [AppComponent],
})
export class AppModule { }

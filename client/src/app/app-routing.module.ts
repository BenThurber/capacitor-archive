import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent} from './views/contact/contact.component';
import { NotImplementedComponent} from './views/not-implemented/not-implemented.component';
import { AboutComponent } from './views/about/about.component';
import { HomeComponent } from './views/home/home.component';
import {CreateManufacturerComponent} from './views/create-manufacturer/create-manufacturer.component';
import {PageNotFoundComponent} from './views/page-not-found/page-not-found.component';
import {ViewManufacturerComponent} from './views/view-manufacturer/view-manufacturer.component';
import {EditManufacturerComponent} from './views/edit-manufacturer/edit-manufacturer.component';
import {CreateCapacitorComponent} from './views/create-capacitor/create-capacitor.component';
import {ViewCapacitorComponent} from './views/view-capacitor/view-capacitor.component';
import {EditCapacitorComponent} from './views/edit-capacitor/edit-capacitor.component';

const routes: Routes = [
  {path: 'not-implemented', component: NotImplementedComponent, data: {title: 'Not Implemented'}},
  {path: '', component: HomeComponent, data: {title: 'Capacitor Archive - Home'}},
  {path: 'about', component: AboutComponent, data: {title: 'Capacitor Archive - About'}},
  {path: 'contact', component: ContactComponent, data: {title: 'Capacitor Archive - Contact'}},
  {path: 'manufacturer/create', component: CreateManufacturerComponent, data: {title: 'Creating Manufacturer'}},
  {path: 'manufacturer/edit/:companyName', component: EditManufacturerComponent, data: {title: 'Editing Manufacturer'}},
  {path: 'manufacturer/view/:companyName', component: ViewManufacturerComponent, data: {title: 'Viewing Manufacturer'}},
  {path: 'capacitor/create', component: CreateCapacitorComponent, data: {title: 'Creating Capacitor'}},
  {path: 'capacitor/create/:companyName', component: CreateCapacitorComponent, data: {title: 'Creating Capacitor'}},
  {path: 'capacitor/create/:companyName/:typeName', component: CreateCapacitorComponent, data: {title: 'Creating Capacitor'}},
  {path: 'capacitor/edit/:companyName/:typeName/:value', component: EditCapacitorComponent, data: {title: 'Editing Capacitor'}},
  {path: 'capacitor/view/:companyName/:typeName', component: ViewCapacitorComponent, data: {title: 'Viewing Capacitor'}},
  {path: 'capacitor/view/:companyName/:typeName/:value', component: ViewCapacitorComponent, data: {title: 'Viewing Capacitor'}},
  {path: 'not-found', component: PageNotFoundComponent, data: {title: 'Page Not Found'}},
  {path: '**', component: PageNotFoundComponent, data: {title: 'Page Not Found'}},  // This must be the last element of the Routes array
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = routes.map(route => route.component);

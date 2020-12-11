import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent} from './views/contact/contact.component';
import { NotImplementedComponent} from './views/not-implemented/not-implemented.component';
import { AboutComponent } from './views/about/about.component';
import { HomeComponent } from './views/home/home.component';
import {CreateManufacturerComponent} from './views/create-manufacturer/create-manufacturer.component';
import {PageNotFoundComponent} from './views/page-not-found/page-not-found.component';
import {ViewManufacturerComponent} from './views/view-manufacturer/view-manufacturer.component';

const routes: Routes = [
  {path: 'not-implemented', component: NotImplementedComponent},
  {path: '', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'manufacturer/create', component: CreateManufacturerComponent},
  {path: 'manufacturer/view/1', component: ViewManufacturerComponent},
  {path: '**', component: PageNotFoundComponent },  // This must be the last element of the Routes array
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = routes.map(route => route.component);

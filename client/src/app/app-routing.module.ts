import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent} from './views/contact/contact.component';
import { NotImplementedComponent} from './views/not-implemented/not-implemented.component';
import {AboutComponent} from './views/about/about.component';

const routes: Routes = [
  {path: 'contact', component: ContactComponent},
  {path: 'not-implemented', component: NotImplementedComponent},
  {path: 'about', component: AboutComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = routes.map(route => route.component);

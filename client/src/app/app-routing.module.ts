import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent} from './views/contact/contact.component';
import { NotImplementedComponent} from './views/not-implemented/not-implemented.component';
import { AboutComponent } from './views/about/about.component';
import { HomeComponent } from './views/home/home.component';

const routes: Routes = [
  {path: 'not-implemented', component: NotImplementedComponent},
  {path: '', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'contact', component: ContactComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = routes.map(route => route.component);

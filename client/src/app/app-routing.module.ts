import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContactComponent} from './contact/contact.component';
import { NotImplementedComponent} from './not-implemented/not-implemented.component';

const routes: Routes = [
  {path: 'contact', component: ContactComponent},
  {path: 'not-implemented', component: NotImplementedComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = routes.map(route => route.component);

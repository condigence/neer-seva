import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

const routes: Routes = [

  { path: '', component: HomeComponent }
  // {
  //   path: 'students',
  //   children: [
  //     { path: 'add', component: AddStudentComponent},
  //     { path: 'list', component: ListStudentComponent },
  //   ]
  // }



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import {SearchpageComponent} from './components/searchpage/searchpage.component';
import { RepoPageComponent } from './components/repo-page/repo-page.component';
import { UserDetailPageComponent } from './components/user-detail-page/user-detail-page.component';
import { SearchuserComponent } from './components/searchuser/searchuser.component';
const routes: Routes = [
{
   path: '',
   component: HomeComponent
},
{
    path : 'searchrepo',
    component: SearchpageComponent
},
{
   path : 'searchuser',
   component: SearchuserComponent
},
{
   path : 'repo/:id',
   component: RepoPageComponent
},
{
   path : 'user/:id',
   component: UserDetailPageComponent
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

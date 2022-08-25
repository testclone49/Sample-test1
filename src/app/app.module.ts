import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchpageComponent } from './components/searchpage/searchpage.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { RepoPageComponent } from './components/repo-page/repo-page.component';
import { HomeComponent } from './components/home/home.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UserDetailPageComponent } from './components/user-detail-page/user-detail-page.component';
import { SearchuserComponent } from './components/searchuser/searchuser.component';


@NgModule({
  declarations: [
    AppComponent,
    SearchpageComponent,
    SearchuserComponent,
    UserDetailPageComponent,
    HeaderComponent,
    FooterComponent,
    RepoPageComponent,
    HomeComponent,
  

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

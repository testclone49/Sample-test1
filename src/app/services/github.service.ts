import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GithubService {

  constructor(private  http : HttpClient) { }

    url : string = "https://api.github.com";

    getUser(userId : string){
       return this.http.get(this.url+'/users/'+userId);
    } 

    getRepo(userId : string){
      return this.http.get(this.url+'/search/repositories?q='+userId);
   } 
    
}

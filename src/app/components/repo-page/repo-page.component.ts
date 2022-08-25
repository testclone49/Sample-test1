import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GithubService } from 'src/app/services/github.service';

@Component({
  selector: 'repo-page',
  templateUrl: './repo-page.component.html',
  styleUrls: ['./repo-page.component.css']
})
export class RepoPageComponent implements OnInit {
  username : string;
  userDetail : any;
  itemCount: any;
  
  imgurl : string;
  name : string;
  bio : string;
  followers : number;
  company : string;
  location : string;

  constructor(private route : ActivatedRoute , private githubService :GithubService , private router : Router ) { }

  ngOnInit(): void {
      this.route.params.subscribe(params =>{
           this.username = params['id'];
           console.log("username",this.username);
       })

       this.githubService.getRepo(this.username).subscribe({
        
         complete : () => {console.log("success!")},
         error:() => {
        
            alert("error ! search again");
           
            this.router.navigate(['searchuser']);
            
         },
         next : (data : any = []) => {
           this.itemCount = data.total_count;
           this.userDetail = data.items;
           console.log(this.userDetail);
           this.bio = this.userDetail.bio;
           this.company = this.userDetail.company;
           this.followers = this.userDetail.followers;
           this.imgurl = this.userDetail.avatar_url;
           this.location = this.userDetail.location;
           this.name = this.userDetail.name;
          }

       })

      

  }

}

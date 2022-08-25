import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GithubService } from 'src/app/services/github.service';

@Component({
  selector: 'app-user-detail-page',
  templateUrl: './user-detail-page.component.html',
  styleUrls: ['./user-detail-page.component.css']
})
export class UserDetailPageComponent implements OnInit {
  username : string;
  userDetail : any;
  
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

       this.githubService.getUser(this.username).subscribe({
        
         complete : () => {console.log("success!")},
         error:() => {
        
            alert("error ! search again");
           
            this.router.navigate(['searchuser']);
            
         },
         next : (data : any = []) => {
           this.userDetail = data;
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

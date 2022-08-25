import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-searchuser',
  templateUrl: './searchuser.component.html',
  styleUrls: ['./searchuser.component.css']
})
export class SearchuserComponent implements OnInit {
     searchform : FormGroup;

    constructor(private route : Router) { } 
    ngOnInit(): void {
        this.searchform = new FormGroup({
             username : new FormControl(
               null,
               [Validators.required]
             )
        })
    }

    sendUser(){
       console.log(this.searchform.value);
       const username = this.searchform.value.username;
       this.route.navigate([`/user/${username}`])
       
    }

}
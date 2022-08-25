import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-searchpage',
  templateUrl: './searchpage.component.html',
  styleUrls: ['./searchpage.component.css']
})
export class SearchpageComponent implements OnInit {
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
       this.route.navigate([`/repo/${username}`])
       
    }

}
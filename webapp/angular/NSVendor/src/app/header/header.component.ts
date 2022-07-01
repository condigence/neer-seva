import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../service/profile.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private profileService: ProfileService) { }
  profile;
  ngOnInit() {

    // let currentUser = localStorage.getItem('currentUser');
    // this.profileService.getProfileByUserById(9).subscribe(data => {
    //   this.profile = data;

    //   console.log(this.profile);
    // });
    
  }

}

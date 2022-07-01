import { Component } from '@angular/core';

@Component({
  selector: 'my-app',
  template : `
    <router-outlet></router-outlet>
  `
})
export class AppComponent  {
  name = 'MyShop';
  public message: string ='';

}



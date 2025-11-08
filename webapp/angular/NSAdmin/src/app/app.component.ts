import { Component, OnInit, Renderer2, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './service/auth.service';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  user: any;
  isUserActive: any;
  title = 'NeerSevaAdmin';
  currentUser: any;  // Sol. for data.id property does not exist
  isSidebarOpen: boolean = false;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private renderer: Renderer2
  ) {
    this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
    // Initialize synchronously from the AuthenticationService current value instead of reading localStorage directly
    this.currentUser = this.authenticationService.currentUserValue;
  }

  // Toggle sidebar visibility by adding/removing the 'toggled' class on #wrapper
  toggleSidebar() {
    const wrapper = document.getElementById('wrapper');
    if (!wrapper) return;
    if (wrapper.classList.contains('toggled')) {
      this.renderer.removeClass(wrapper, 'toggled');
      const overlay = document.getElementById('sidebar-overlay');
      if (overlay) {
        this.renderer.removeClass(overlay, 'visible');
        this.renderer.setAttribute(overlay, 'aria-hidden', 'true');
      }
      this.isSidebarOpen = false;
      try { localStorage.setItem('sidebarToggled','false'); } catch(e) {}
    } else {
      this.renderer.addClass(wrapper, 'toggled');
      const overlay = document.getElementById('sidebar-overlay');
      if (overlay) {
        this.renderer.addClass(overlay, 'visible');
        this.renderer.setAttribute(overlay, 'aria-hidden', 'false');
      }
      this.isSidebarOpen = true;
      try { localStorage.setItem('sidebarToggled','true'); } catch(e) {}
      setTimeout(() => {
        const firstLink = document.querySelector('#sidebar-wrapper a');
        if (firstLink) { (firstLink as HTMLElement).focus(); }
      }, 50);
    }
  }

  // Close the sidebar (used by clicking overlay)
  closeSidebar() {
    const wrapper = document.getElementById('wrapper');
    if (!wrapper) return;
    this.renderer.removeClass(wrapper, 'toggled');
    const overlay = document.getElementById('sidebar-overlay');
    if (overlay) {
      this.renderer.removeClass(overlay, 'visible');
      this.renderer.setAttribute(overlay, 'aria-hidden', 'true');
    }
    this.isSidebarOpen = false;
    try { localStorage.setItem('sidebarToggled','false'); } catch(e) {}
    const toggle = document.querySelector('.menu-icon');
    if (toggle) { (toggle as HTMLElement).focus(); }
  }

  // Close the sidebar when Escape is pressed anywhere in the document
  @HostListener('document:keydown.escape', ['$event'])
  onEscape(event: KeyboardEvent) {
    if (this.isSidebarOpen) {
      this.closeSidebar();
    }
  }

  ngOnInit(): void {
    // Use the AuthenticationService currentUser value (keeps auth centralized in the AuthGuard)
    const parsed = this.authenticationService.currentUserValue;
    if (!parsed || !parsed.id) {
      // No logged-in user at app init; nothing to fetch here. AuthGuard handles route protection.
      return;
    }

    this.userService.getUserById(parsed.id).subscribe(data => {
      this.user = data;
      if (this.user.active) {
        this.isUserActive = true;

        // Update stored currentUser's picture with the fetched user profile picture
        try {
          // Merge fetched profile picture into currentUser and notify subscribers
          const cu = this.authenticationService.currentUserValue || {} as any;
          if (this.user.pic) {
            cu.pic = this.user.pic;
          }
          this.authenticationService.setCurrentUser(cu as any);
          this.currentUser = cu;
        } catch (e) {
          console.error('Error updating currentUser pic:', e);
        }

        console.log(this.user);
       // console.log(this.isUserActive);
      } else {
        this.isUserActive = false;
        //console.log(this.isUserActive);
        console.log("Please complete Profile!");
      }
    });
   // console.log(this.currentUser);
    
    // Apply persisted sidebar state (if any)
    try {
      const persisted = localStorage.getItem('sidebarToggled');
      this.isSidebarOpen = persisted === 'true';
      const wrapper = document.getElementById('wrapper');
      const overlay = document.getElementById('sidebar-overlay');
      if (this.isSidebarOpen && wrapper) {
        this.renderer.addClass(wrapper, 'toggled');
        if (overlay) {
          this.renderer.addClass(overlay, 'visible');
          this.renderer.setAttribute(overlay, 'aria-hidden', 'false');
        }
      } else if (wrapper) {
        this.renderer.removeClass(wrapper, 'toggled');
        if (overlay) {
          this.renderer.removeClass(overlay, 'visible');
          this.renderer.setAttribute(overlay, 'aria-hidden', 'true');
        }
      }
    } catch(e) {
      // ignore
    }

  }
  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }
}

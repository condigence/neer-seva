import { Component, OnInit, AfterViewInit, Renderer2, HostListener } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthenticationService } from './service/auth.service';
import { UserService } from './service/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, AfterViewInit {

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
    // this.currentUser = this.authenticationService.currentUserValue;
    // Close any open dropdowns after successful navigation to keep the UI tidy
    this.router.events.subscribe(evt => {
      if (evt instanceof NavigationEnd) {
        this.closeOpenDropdowns();
      }
    });
    // Close dropdowns immediately when a dropdown item is clicked (delegated)
    this.renderer.listen('document', 'click', (event: Event) => {
      try {
        const target = event.target as HTMLElement | null;
        if (!target) return;

        // If the click originated from a dropdown toggle, ignore — let bootstrap handle it
        if (target.closest && target.closest('.dropdown-toggle')) return;

        // If clicked inside a dropdown-menu (likely a dropdown item), close menus
        if (target.closest && target.closest('.dropdown-menu')) {
          // Slight delay so any navigation or click handlers run first
          setTimeout(() => this.closeOpenDropdowns(), 0);
        }
      } catch (e) {
        // ignore
      }
    });
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
      const toggleBtn = document.querySelector('.toggle-menu');
      if (toggleBtn) { this.renderer.setAttribute(toggleBtn as any, 'aria-expanded', 'false'); }
      this.isSidebarOpen = false;
      try { localStorage.setItem('sidebarToggled','false'); } catch(e) {}
    } else {
      this.renderer.addClass(wrapper, 'toggled');
      const overlay = document.getElementById('sidebar-overlay');
      if (overlay) {
        this.renderer.addClass(overlay, 'visible');
        this.renderer.setAttribute(overlay, 'aria-hidden', 'false');
      }
      const toggleBtn = document.querySelector('.toggle-menu');
      if (toggleBtn) { this.renderer.setAttribute(toggleBtn as any, 'aria-expanded', 'true'); }
      this.isSidebarOpen = true;
      try { localStorage.setItem('sidebarToggled','true'); } catch(e) {}
      setTimeout(() => {
        const firstLink = document.querySelector('#sidebar-wrapper a');
        if (firstLink) { (firstLink as HTMLElement).focus(); }
      }, 50);
    }
  }
  
  /**
   * Programmatically close any open Bootstrap dropdowns.
   * This helps when Angular navigation happens and we want dropdowns to vanish
   * (some dropdowns remain visible depending on markup or JS race conditions).
   */
  public closeOpenDropdowns(): void {
    try {
      // Close any open .dropdown-menu
      const openMenus = document.querySelectorAll('.dropdown-menu.show');
      openMenus.forEach(menu => this.renderer.removeClass(menu, 'show'));

      // Remove .show from parent .dropdown elements
      const openParents = document.querySelectorAll('.dropdown.show');
      openParents.forEach(parent => this.renderer.removeClass(parent, 'show'));

      // Ensure toggles reflect collapsed state (aria-expanded=false)
      const toggles = document.querySelectorAll('.dropdown-toggle[aria-expanded]');
      toggles.forEach(t => this.renderer.setAttribute(t as HTMLElement, 'aria-expanded', 'false'));
    } catch (e) {
      // If anything goes wrong, don't break navigation — console for debug
      // eslint-disable-next-line no-console
      console.debug('closeOpenDropdowns error', e);
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
    const toggleBtn = document.querySelector('.toggle-menu');
    if (toggleBtn) { (toggleBtn as HTMLElement).focus(); }
  }

  // Close the sidebar when Escape is pressed anywhere in the document
  @HostListener('document:keydown.escape', ['$event'])
  onEscape(event: KeyboardEvent) {
    if (this.isSidebarOpen) {
      this.closeSidebar();
    }
  }

  ngAfterViewInit(): void {
    // Initialize sidebar menu after view is rendered
    if (typeof (window as any).$ !== 'undefined' && typeof (window as any).$.sidebarMenu === 'function') {
      (window as any).$('.sidebar-menu').each(function() {
        (window as any).$.sidebarMenu((window as any).$(this));
      });
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
          // this.currentUser = cu;
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
        const toggleBtn = document.querySelector('.toggle-menu');
        if (toggleBtn) { this.renderer.setAttribute(toggleBtn as any, 'aria-expanded', 'true'); }
      } else if (wrapper) {
        this.renderer.removeClass(wrapper, 'toggled');
        if (overlay) {
          this.renderer.removeClass(overlay, 'visible');
          this.renderer.setAttribute(overlay, 'aria-hidden', 'true');
        }
        const toggleBtn = document.querySelector('.toggle-menu');
        if (toggleBtn) { this.renderer.setAttribute(toggleBtn as any, 'aria-expanded', 'false'); }
      }
    } catch(e) {
      // ignore
    }

  }
  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  // Navigate to profile from inline header dropdown
  goToProfile(event: Event) {
    event.preventDefault();
    // eslint-disable-next-line no-console
    console.log('goToProfile clicked (app component)');
    this.closeOpenDropdowns();
    setTimeout(() => {
      this.router.navigateByUrl('/profile/my-profile').catch(err => {
        // eslint-disable-next-line no-console
        console.error('Navigation error to profile:', err);
      });
    }, 80);
  }

  // Handle Space key on the toggle control to emulate native button behavior
  onToggleKey(event: Event) {
    // Prevent the page from scrolling when Space is pressed
    event.preventDefault();
    this.toggleSidebar();
  }
}

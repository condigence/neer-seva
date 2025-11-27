import { Component, OnInit, Renderer2, AfterViewInit, OnDestroy } from '@angular/core';
import Chart from 'chart.js/auto';
import { UserService } from '../service/user.service';
import { AddressService } from '../service/address.service';
import { OrderService } from '../service/order.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, AfterViewInit, OnDestroy {
  
  userscount: any;
  userAsCustomer: any;
  userAsVendor: any;
  totalOrders: any;
  top4Orders: any;
  fiveUsers: any;
  totalRevenue: any | number = 12500; // Example total revenue
  constructor(private userService: UserService, private orderService: OrderService, private addressService: AddressService, private renderer: Renderer2) { }
  

  ngOnInit() {

    this.userService.getAllUsers()
    .subscribe(data => {
      this.userscount = data;  
       
    });
    this.userService.getTop5CustomerCount()
    .subscribe(data => {
      this.userAsCustomer = data;   

   
    });
    this.userService.getTop5VendorCount()
    .subscribe(data => {
      this.userAsVendor = data; 
       // Loop through each vendor and get their address
    this.userAsVendor.forEach(vendor => {
    this.addressService.getAddressById(vendor.addressId).subscribe(address => {
      vendor.address = address; // Attach address to vendor object
    });
  });
  
    });

    
    

    this.orderService.getAllOrders()
    .subscribe(data => {
      this.totalOrders = data;  
    this.top4Orders = [...this.totalOrders].reverse().slice(0, 4);

  console.log(this.top4Orders);
    });    
  }

  // Chart.js instances
  private barChart: any = null;
  private doughnutChart: any = null;

  ngAfterViewInit(): void {
    try {
      const barEl = document.getElementById('home-bar-chart') as HTMLCanvasElement | null;
      if (barEl && (barEl as any).getContext) {
        const ctx = (barEl as HTMLCanvasElement).getContext('2d') as CanvasRenderingContext2D;
        this.barChart = new Chart(ctx, {
          type: 'bar',
          data: {
            labels: [1,2,3,4,5,6,7,8],
            datasets: [
              { label: 'New Orders', data: [40,30,60,35,60,25,50,40], backgroundColor: '#11cdef', borderColor: '#11cdef', borderWidth: 1 },
              { label: 'Pending', data: [50,60,40,70,35,75,30,20], backgroundColor: '#e8e8e8', borderColor: '#e8e8e8', borderWidth: 1 }
            ]
          },
          options: {
            responsive: true,
            plugins: { legend: { display: true, position: 'bottom' }, tooltip: { enabled: true } },
            scales: { x: { stacked: true }, y: { stacked: true } }
          }
        });
      }

      const doughEl = document.getElementById('home-doughnut-chart') as HTMLCanvasElement | null;
      if (doughEl && (doughEl as any).getContext) {
        const ctx2 = (doughEl as HTMLCanvasElement).getContext('2d') as CanvasRenderingContext2D;
        this.doughnutChart = new Chart(ctx2, {
          type: 'doughnut',
          data: {
            labels: ['kinley', 'Bisleri', 'Bailley'],
            datasets: [{ backgroundColor: ['#5e72e4','#ff2fa0','#2dce89'], data: [25,50,25], borderWidth: 1 }]
          },
          options: {
            responsive: true,
            cutout: '25%',
            plugins: { legend: { display: true, position: 'right' }, tooltip: { enabled: true } }
          }
        });
      }
    } catch (e) {
      // swallow chart init errors to avoid breaking the dashboard
      console.error('Chart initialization error', e);
    }
  }

  ngOnDestroy(): void {
    try {
      if (this.barChart) { this.barChart.destroy(); this.barChart = null; }
      if (this.doughnutChart) { this.doughnutChart.destroy(); this.doughnutChart = null; }
    } catch (e) { }
  }




   

  
  // Close dropdowns immediately when a dropdown item is clicked
  public closeOpenDropdowns(): void {
    try {
      const openMenus = document.querySelectorAll('.dropdown-menu.show');
      openMenus.forEach(menu => this.renderer.removeClass(menu, 'show'));

      const openParents = document.querySelectorAll('.dropdown.show');
      openParents.forEach(parent => this.renderer.removeClass(parent, 'show'));

      const toggles = document.querySelectorAll('.dropdown-toggle[aria-expanded]');
      toggles.forEach(t => this.renderer.setAttribute(t as HTMLElement, 'aria-expanded', 'false'));
    } catch (e) {
      // ignore
    }
  }

}

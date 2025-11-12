import { Component, OnInit, Renderer2, AfterViewInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  userscount: any;
  userAsCustomer: any;
  userAsVendor: any;
  totalOrders: any;
  fiveUsers: any;
  constructor(private userService: UserService, private renderer: Renderer2) { }

  // --- ng2-charts sample data and options ---
  public barChartType: ChartType = 'bar';
  public barChartOptions: ChartOptions = {
    responsive: true,
    plugins: { legend: { display: true, position: 'bottom' } },
    scales: { x: { stacked: true }, y: { stacked: true } }
  } as ChartOptions;
  public barChartData: ChartConfiguration['data'] = {
    labels: [1,2,3,4,5,6,7,8],
    datasets: [
      { label: 'New Orders', data: [40,30,60,35,60,25,50,40], backgroundColor: '#11cdef' },
      { label: 'Pending', data: [50,60,40,70,35,75,30,20], backgroundColor: '#e8e8e8' }
    ]
  };

  public doughnutChartType: ChartType = 'doughnut';
  public doughnutChartOptions: ChartOptions = {
    responsive: true,
    cutout: '25%',
    plugins: { legend: { display: true, position: 'right' } }
  } as ChartOptions;
  public doughnutChartData: ChartConfiguration['data'] = {
    labels: ['kinley','Bisleri','Bailley'],
    datasets: [{ data: [25,50,25], backgroundColor: ['#5e72e4','#ff2fa0','#2dce89'] }]
  };

  ngOnInit() {
    this.userService.getAllUsersCount()
    .subscribe(data => {
      this.userscount = data;         
    });
    this.userService.getAllCustomerCount()
    .subscribe(data => {
      this.userAsCustomer = data;      
    });
    this.userService.getAllVendorCount()
    .subscribe(data => {
      this.userAsVendor = data;      
    });
    this.userService.getAllOrderCount()
    .subscribe(data => {
      this.totalOrders = data;      
    });    
  }

  ngAfterViewInit(): void {
    try {
      // initialize dashboard-chart-1 (bar stacked)
      const el1 = document.getElementById('dashboard-chart-1') as HTMLCanvasElement | null;
      if (el1 && (el1 as any).getContext) {
        const ctx = el1.getContext('2d') as CanvasRenderingContext2D;
        // create a bar chart similar to the original index.js
        // use sample/demo data; later you can replace with API-driven values
        new Chart(ctx, {
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
            plugins: { legend: { display: true, position: 'bottom', labels: { boxWidth: 12 } }, tooltip: { enabled: true } },
            scales: { x: { stacked: true }, y: { stacked: true } }
          }
        });
      }

      // initialize dashboard-chart-2 (doughnut)
      const el2 = document.getElementById('dashboard-chart-2') as HTMLCanvasElement | null;
      if (el2 && (el2 as any).getContext) {
        const ctx2 = el2.getContext('2d') as CanvasRenderingContext2D;
        new Chart(ctx2, {
          type: 'doughnut',
          data: {
            labels: ['kinley', 'Bisleri', 'Bailley'],
            datasets: [{ backgroundColor: ['#5e72e4','#ff2fa0','#2dce89'], data: [25,50,25], borderWidth: 1 }]
          },
          options: {
            responsive: true,
            cutout: '25%',
            plugins: { legend: { display: true, position: 'right', labels: { boxWidth: 12 } }, tooltip: { enabled: true } }
          }
        });
      }
    } catch (e) {
      // don't break the app if Chart isn't available
      console.debug('Chart init error', e);
    }
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

import { Component, OnInit, Renderer2, AfterViewInit, OnDestroy } from '@angular/core';
import Chart from 'chart.js/auto';
import { UserService } from '../service/user.service';
import { AddressService } from '../service/address.service';
import { OrderService } from '../service/order.service';
import { ItemService } from '../service/item.service';
import { forkJoin, map } from 'rxjs';

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
  top5Orders: any;
  fiveUsers: any;
  totalRevenue: any | number = 2500; // Example total revenue
  constructor(private userService: UserService, private orderService: OrderService, private addressService: AddressService, private renderer: Renderer2, private itemService: ItemService) { }
  

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

  // ================================================
    // LOAD 5 MOST RECENT ORDER ITEMS WITH ITEM DETAILS
    // ================================================

    this.orderService.getAllOrders().pipe(
      map(orders => {
    
    // ================================
    // 1️⃣ Compute TOTAL ORDER COUNT
    // ================================
    this.totalOrders = orders.length;

    // ================================
    // 2️⃣ Compute TOTAL REVENUE
    // orderGrandTotal may be null → treat as 0
    // ================================
    this.totalRevenue = orders.reduce((sum, o) =>
      sum + (o.orderGrandTotal || 0), 0
    );

    // Log (optional)
    console.log("Total Orders:", this.totalOrders);
    console.log("Total Revenue:", this.totalRevenue);

    // ================================
    // 3️⃣ Return flattened list (for top 5 logic)
    // ================================
    return this.flattenOrderItems(orders);

  })  // Step 1: Flatten & sort
    )
    .subscribe(flatItems => {

      this.top5Orders = flatItems.slice(0, 5);      // Step 2: Take top 5
      console.log('Top 5 Orders:', this.top5Orders);

      const ids = [...new Set(this.top5Orders.map(x => x.itemId))]; // unique itemIds

      if (ids.length === 0) return;

      // Step 3: create /items/{id} calls for each id
      const calls = ids.map(id => this.itemService.getItemById(id));

      // Step 4: run all calls in parallel
      forkJoin(calls).subscribe(items => {

        const itemMap = new Map(items.map(i => [i.id, i]));

        // Step 5: attach product name + image
        this.top5Orders = this.top5Orders.map(row => ({
          ...row,
          productName: itemMap.get(row.itemId)?.name || 'Loading...',
          productImage: itemMap.get(row.itemId)?.pic || null
        }));
      });
    });

  }

  // ---------------------------------------------------------------------
  // FLATTEN ORDER + SORT
  // ---------------------------------------------------------------------

  flattenOrderItems(orders: any[]) {
    const flatList: any[] = [];

    (orders || []).forEach(order => {
      (order.orderDetail || []).forEach(detail => {
        flatList.push({
          date: order.orderDate,
          time: order.orderTime,
          status: order.orderStatus,
          qty: detail.orderItemQuantity,
          amount: order.orderGrandTotal,
          itemId: detail.orderItemId,
          orderId: order.orderId
        });
      });
    });

    // Sort latest first
    flatList.sort((a, b) => {
      const aTime = new Date(`${a.date}T${a.time || '00:00:00'}`).getTime();
      const bTime = new Date(`${b.date}T${b.time || '00:00:00'}`).getTime();
      return bTime - aTime;
    });

    return flatList;
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

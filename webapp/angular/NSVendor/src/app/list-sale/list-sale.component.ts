import { Component, OnInit } from '@angular/core';
import { OrderService } from '../service/order.service';

export interface SaleRow {
  orderId: number;
  orderDate: string;
  deliveryStatus: string;

  itemId: number;
  itemName: string;
  quantity: number;
  price: number;     // item price
  subtotal: number;

  customerName: string;
  customerContact: string;

  totalAmount: number;    // order total
}


@Component({
  selector: 'app-list-sale',
  templateUrl: './list-sale.component.html',
  styleUrls: ['./list-sale.component.scss']
})
export class ListSaleComponent implements OnInit {
 rawOrders: any[] = [];   // backend response
  salesList: any[] = [];   // grouped sales list
  filteredSales: any[] = []; // filtered items shown on table

  totalQuantity: number = 0;
  totalRevenue: number = 0;

  selectedFilter: string = 'daily'; // default filter

  constructor(private orderService: OrderService ) {}

  ngOnInit() {
    this.loadSales();
  }

 loadSales() {
  const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');

  this.orderService.getOrderByUserById(currentUser.id)
    .subscribe((orders: any[]) => {
      
      this.rawOrders = orders;
console.log("RAW ORDERS RESPONSE:", this.rawOrders);

      // NEW: build clean sale rows
      this.salesList = this.transformOrdersToSalesRows(orders);

      // Apply default daily filter
      this.applyFilter('daily');
    });
}



  // ----------------------------------------------------------
  // STEP 1: Transform raw orders into sales rows and group them
  // ----------------------------------------------------------

 private transformOrdersToSalesRows(rawOrders: any[]) {
  const rows: any[] = [];

  rawOrders.forEach(order => {

    // Ensure orderDetail + items exist
    if (!order.orderDetail || !order.orderDetail.items) {
      console.warn("Order missing orderDetail.items:", order);
      return;
    }

    const customer = order.orderDetail.customer || {};
    const vendor = order.orderDetail.vendor || {};

    const customerName = customer.name || "NA";
    const customerContact = customer.contact || "NA";
    const vendorName = vendor.name || "NA";

    const items = order.orderDetail.items;

    items.forEach((item: any) => {
      const quantity = item.quantity || item.orderItemQuantity || 0;
      const price = item.dispPrice || item.price || item.orderItemPrice || 0;

      rows.push({
        orderId: order.orderId,
        orderDate: order.orderDate,
        orderStatus: order.orderStatus,
        deliveryStatus: order.orderDeliveryStatus,

        customerName,
        customerContact,
        vendorName,

        itemId: item.id,
        itemName: item.name,
        itemType: item.type || "N/A",

        quantity,
        price,
        subtotal: quantity * price,

        totalOrderAmount: order.orderGrandTotal || 0
      });
    });

  });

  console.log("TRANSFORMED ROWS:", rows);
  return rows;
}




  // ----------------------------------------------------------
  // STEP 2: Filter functions (daily / weekly / monthly / yearly)
  // ----------------------------------------------------------
 applyFilter(type: string) {
  this.selectedFilter = type;

  const today = new Date();
  let start = new Date();

  if (type === 'daily') start = new Date(today.getFullYear(), today.getMonth(), today.getDate());
  if (type === 'weekly') start = new Date(today.setDate(today.getDate() - 7));
  if (type === 'monthly') start = new Date(today.getFullYear(), today.getMonth(), 1);
  if (type === 'yearly') start = new Date(today.getFullYear(), 0, 1);

  this.filteredSales = this.salesList.filter(sale =>
    new Date(sale.orderDate) >= start
  );

  this.calculateTotals();
}


  // ----------------------------------------------------------
  // STEP 3: Calculate Totals for bottom row
  // ----------------------------------------------------------
  calculateTotals() {
    this.totalQuantity = this.filteredSales
      .reduce((sum, item) => sum + item.quantitySold, 0);

    this.totalRevenue = this.filteredSales
      .reduce((sum, item) => sum + item.totalRevenue, 0);
  }

}


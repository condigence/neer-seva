import {
  Component,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ViewChild,
  ElementRef
} from '@angular/core';

import Chart from 'chart.js/auto';
import { UserService } from '../service/user.service';
import { ItemService } from '../service/item.service';
import { forkJoin, Subject, of } from 'rxjs';
import { takeUntil, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard3',
  templateUrl: './dashboard3.component.html',
  styleUrls: ['./dashboard3.component.scss']
})
export class Dashboard3Component implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild('barCanvas') barCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineCanvas') lineCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('doughnutCanvas') doughnutCanvas!: ElementRef<HTMLCanvasElement>;

  private destroy$ = new Subject<void>();

  private barChart?: Chart;
  private lineChart?: Chart;
  private doughnutChart?: Chart;

  private viewReady = false;
  private tempData: any = null;

  constructor(
    private userService: UserService,
    private itemService: ItemService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    this.viewReady = true;

    if (this.tempData) {
      const { labels, newOrders, pending, sales, best } = this.tempData;

      this.drawCharts(labels, newOrders, pending, sales);
      this.createDoughnutChart(best.labels, best.data);

      this.tempData = null;
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.destroyCharts();
  }

  private loadDashboardData(): void {
    forkJoin({
      orders: this.userService.getAllOrderCount().pipe(catchError(() => of([]))),
      items: this.itemService.getAllItems().pipe(catchError(() => of([])))
    })
      .pipe(takeUntil(this.destroy$))
      .subscribe(({ orders, items }) => {

        // Always treat as array
        const orderList = Array.isArray(orders) ? orders : [orders];
        const itemList = Array.isArray(items) ? items : [items];

        // Clean nulls
        const cleanOrders = orderList.filter(o => !!o);
        const cleanItems = itemList.filter(i => !!i);

        // Process bar + line chart
        const grouped: any = {};

        cleanOrders.forEach(order => {
          const date = order.orderDate || 'Unknown';

          if (!grouped[date]) {
            grouped[date] = { newOrders: 0, pending: 0, sales: 0 };
          }

          if (order.orderStatus === 'CONFIRMED') {
            grouped[date].newOrders++;
            grouped[date].sales += Number(order.orderGrandTotal || 0);
          }

          if (order.orderStatus === 'PENDING') {
            grouped[date].pending++;
          }
        });

        const labels = Object.keys(grouped).sort();
        const newOrders = labels.map(d => grouped[d].newOrders);
        const pending = labels.map(d => grouped[d].pending);
        const sales = labels.map(d => grouped[d].sales);

        const best = this.calculateBestSelling(cleanOrders, cleanItems);

        if (!this.viewReady) {
          this.tempData = { labels, newOrders, pending, sales, best };
          return;
        }

        this.drawCharts(labels, newOrders, pending, sales);
        this.createDoughnutChart(best.labels, best.data);
      });
  }

  private calculateBestSelling(orders: any[], items: any[]) {
    const itemNames: Record<number, string> = {};

    items.forEach(i => {
      if (i?.id) itemNames[i.id] = i.name || `Item ${i.id}`;
    });

    const sold: Record<string, number> = {};

    orders.forEach(order => {
      if (!Array.isArray(order.orderDetail)) return;

      order.orderDetail.forEach(d => {
        const itemId = d.orderItemId;
        const qty = Number(d.orderItemQuantity || 1);
        const name = itemNames[itemId] || `Item ${itemId}`;

        sold[name] = (sold[name] || 0) + qty;
      });
    });

    return {
      labels: Object.keys(sold),
      data: Object.values(sold)
    };
  }

  private destroyCharts() {
    this.barChart?.destroy();
    this.lineChart?.destroy();
    this.doughnutChart?.destroy();
  }

  private drawCharts(labels: string[], newOrders: number[], pending: number[], sales: number[]) {
    this.createBarChart(labels, newOrders, pending);
    this.createLineChart(labels, sales);
  }

  private createBarChart(labels: string[], newOrders: number[], pending: number[]) {
    const ctx = this.barCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    this.barChart?.destroy();

    this.barChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          { label: 'New Orders', data: newOrders, backgroundColor: '#48d9f3ff' },
          { label: 'Pending', data: pending, backgroundColor: '#fd11c2ff' }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'bottom' } },
        scales: { x: { stacked: true }, y: { stacked: true } }
      }
    });
  }

  private createLineChart(labels: string[], sales: number[]) {
    const ctx = this.lineCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    this.lineChart?.destroy();

    this.lineChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: 'Sales Report',
          data: sales,
          borderColor: '#2564f5',
          backgroundColor: 'rgba(37,117,252,0.3)',
          fill: true,
          tension: 0.3
        }]
      },
      options: { responsive: true, maintainAspectRatio: false ,plugins: {
  legend: {
    labels: {
      usePointStyle: true,
      pointStyle: 'rect'
    }
  }
}}
    });
  }

  private createDoughnutChart(labels: string[], data: number[]) {
    const ctx = this.doughnutCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    this.doughnutChart?.destroy();

    this.doughnutChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [{ data, backgroundColor: ['#5e72e4', '#ff2fa0', '#2dce89', '#f5365c'] }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: { legend: { position: 'right' } },
        cutout: '30%'
      }
    });
  }
}

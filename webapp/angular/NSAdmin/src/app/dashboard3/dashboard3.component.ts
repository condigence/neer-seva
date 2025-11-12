import { Component, OnInit } from '@angular/core';
import { ChartConfiguration, ChartOptions, ChartType } from 'chart.js';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-dashboard3',
  templateUrl: './dashboard3.component.html',
  styleUrls: ['./dashboard3.component.scss']
})
export class Dashboard3Component implements OnInit {

  // bar chart (dashboard3-chart-1 sample)
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

  // doughnut chart (dashboard3-chart-2 sample)
  public doughnutChartType: ChartType = 'doughnut';
  public doughnutChartOptions: ChartOptions = { responsive: true, cutout: '25%', plugins: { legend: { display: true, position: 'right' } } } as ChartOptions;
  public doughnutChartData: ChartConfiguration['data'] = {
    labels: ['Jeans','T-Shirts','Shoes','Lingerie','Laptops','Mobiles'],
    datasets: [{ data: [25,50,25,25,15,10], backgroundColor: ['#5e72e4','#ff2fa0','#2dce89','#f5365c','#fb6340','#11cdef'] }]
  };

  // line chart (dashboard3-chart-3 sample)
  public lineChartType: ChartType = 'line';
  public lineChartOptions: ChartOptions = { responsive: true, plugins: { legend: { display: false } } } as ChartOptions;
  public lineChartData: ChartConfiguration['data'] = {
    labels: ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30'],
    datasets: [{ label: 'Sales Report', data: [6,4,6,5,12,8,12,15,6,8,6,12,20,10,15,8,16,10,15,6,5,12,8,10,17,6,7,6,10,0], backgroundColor: 'rgba(37,117,252,0.3)', borderColor: '#2564f5', fill: true }]
  };

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    // sample component — data currently static (from original index3.js). Replace with API calls if needed.
  }

}

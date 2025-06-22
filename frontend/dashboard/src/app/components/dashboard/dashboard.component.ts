import { CommonModule } from '@angular/common'; 
import { Component, OnInit, ViewChild } from '@angular/core';
import { SensorData, SensorService } from '../../services/sensor.service';
import { ChangeDetectorRef } from '@angular/core';
import { ChartConfiguration, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  data: SensorData | null = null;
  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;
  
  temperatureData: number[] = [];
  humidityData: number[] = [];
  labels: string[] = [];

  public lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: this.labels,
    datasets: [
      {
        data: this.temperatureData,
        label: 'Temperatura (°C)',
        fill: false,
        tension: 0.3,
      },
      {
        data: this.humidityData,
        label: 'Umidade (%)',
        fill: false,
        tension: 0.3,
      }
    ]
  };

  public lineChartOptions: ChartOptions<'line'> = {
    responsive: true,
    animations: {
       x: {
        type: 'number',
        easing: 'linear',
        duration: 400
      },
      y: {
        type: 'number',
        easing: 'linear',
        duration: 0
      }
    },
    scales: {
      y: {
        beginAtZero: true
      }
    }
  };

  constructor(private sensorService: SensorService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchSensorData();
    this.fetchDataPeriodically();
  }

  fetchSensorData(): void {
    this.sensorService.getSensorData().subscribe({
      next: (data: SensorData) => {
        const now = new Date(data.timestamp);
        const timeLabel = now.toLocaleTimeString();

        if (this.labels.includes(timeLabel)) {
          return;
        }

        this.labels.push(timeLabel);
        this.temperatureData.push(data.temperature);
        this.humidityData.push(data.humidity);        

        if (this.labels.length > 10) {
          this.labels.shift();
          this.temperatureData.shift();
          this.humidityData.shift();
        }

        this.lineChartData = {
        labels: [...this.labels],
        datasets: [
          {
            ...this.lineChartData.datasets[0],
            data: [...this.temperatureData]
          },
          {
            ...this.lineChartData.datasets[1],
            data: [...this.humidityData]
          }
        ]
      };

        this.cdr.detectChanges();
        this.chart?.update();
      },
      error: err => console.error('Erro ao obter dados do sensor:', err)
    });
  }

  fetchDataPeriodically(): void {
    setInterval(() => this.fetchSensorData(), 5000);
  } 
}
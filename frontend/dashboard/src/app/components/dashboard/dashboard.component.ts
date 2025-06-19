import { CommonModule } from '@angular/common'; 
import { Component, OnInit } from '@angular/core';
import { SensorData, SensorService } from '../../services/sensor.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  data: SensorData | null = null;

  constructor(private sensorService: SensorService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchDataPeriodically();
  }

  fetchDataPeriodically() {
    setInterval(() => {
      this.sensorService.getSensorData().subscribe({
        next: result => {
          this.data = result;
          this.cdr.detectChanges();
          console.log('Sensor data received:');
          console.log('humidity:', this.data.humidity);
          console.log('temperature:', this.data.temperature);
          console.log('timestamp:', this.data.timestamp);
        },
        error: err => console.error('Erro ao obter dados do sensor:', err)
      });
    }, 5000); 
  }

}
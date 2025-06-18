import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SensorData, SensorService } from '../../services/sensor.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  data: SensorData | null = null;

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.fetchDataPeriodically();
  }

  fetchDataPeriodically() {
    setInterval(() => {
      this.sensorService.getSensorData().subscribe({
        next: result => this.data = result,
        error: err => console.error('Erro ao obter dados do sensor:', err)
      });
    }, 5000); 
  }

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface SensorData {
  temperature: number;
  humidity: number;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class SensorService {
   private apiUrl = 'http://localhost:9090/sensor';

  constructor(private http: HttpClient) {}

  getSensorData(): Observable<SensorData> {
    return this.http.get<SensorData>(this.apiUrl);
  }
}

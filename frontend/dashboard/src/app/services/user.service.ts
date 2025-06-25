import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  name: string;
  email: string;
  role: string;
}

export interface UpdatedUser {
  id: number;
  name: string;
  password?: string;
  email: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = 'http://localhost:9090/users';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }

  updateUser(id: number, data: Partial<UpdatedUser>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, data);
  }
}
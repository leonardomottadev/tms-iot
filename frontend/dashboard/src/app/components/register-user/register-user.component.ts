import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register-user',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent {
  name = '';
  email = '';
  password = '';
  role = '';

  successMessage = '';
  errorMessage = '';

  constructor(private http: HttpClient, private router: Router) {}

  registerUser() {
    if (!this.name || !this.email || !this.password || this.password.length < 6) {
      this.errorMessage = "Preencha todos os campos corretamente.";
      this.successMessage = '';
      return;
    }

    const userData = {
      name: this.name,
      email: this.email,
      password: this.password,
      role: 'USER'
    };

    this.http.post('http://localhost:9090/users', userData).subscribe({
      next: () => {
        this.successMessage = "Usuário cadastrado com sucesso!";
        this.errorMessage = '';
        this.name = '';
        this.email = '';
        this.password = '';
        this.role = '';
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Erro ao registrar usuário:', error);
        this.successMessage = '';
        this.errorMessage = error?.error?.message || 'Erro ao registrar usuário.';
      }
    });
  }
}

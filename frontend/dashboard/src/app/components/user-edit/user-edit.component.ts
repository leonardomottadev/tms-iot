import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserService, User } from '../../services/user.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {
  form!: FormGroup;
  userId!: number;
  user!: User;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    public  router: Router
  ) {}

  ngOnInit(): void {
    console.log('COmponente iniciado');
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        console.log('Usuário carregado:', user);
        this.user = user;
        this.form = this.fb.group({
          name: [user.name],
          //email: [user.email],
          password: ['']
        });
        this.cdr.detectChanges();
      },
      error: () => alert('Erro ao carregar dados do usuário')
    });
  }

  save() {
    const formData = this.form.value;
    if (!formData.password || formData.password.trim() === '') {
      delete formData.password;
    }

    this.userService.updateUser(this.userId, formData).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Erro ao salvar dados')
    });
  }

  deleteUser() {
    if (confirm('Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.')) {
      this.userService.deleteUser(this.userId).subscribe({
        next: () => {
          alert('Usuário excluído com sucesso!');
          this.router.navigate(['/login']);
        },
        error: () => alert('Erro ao excluir usuário.')
      });
    }
  }
}

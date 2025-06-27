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
    this.userId = Number(this.route.snapshot.paramMap.get('id'));

    this.userService.getCurrentUser().subscribe({
      next: (user) => {
        this.user = user;
        this.form = this.fb.group({
          name: [user.name],
          //email: [user.email],
          password: ['']
        });
        this.cdr.detectChanges();
      },
      error: () => alert('Error loading user data')
    });
  }

  save() {
    const formData = this.form.value;
    if (!formData.password || formData.password.trim() === '') {
      delete formData.password;
    }

    this.userService.updateUser(this.userId, formData).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => alert('Error saving data')
    });
  }

  deleteUser() {
    if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
      this.userService.deleteUser(this.userId).subscribe({
        next: () => {
          alert('User successfully deleted!');
          this.router.navigate(['/login']);
        },
        error: () => alert('Error deleting user.')
      });
    }
  }

}

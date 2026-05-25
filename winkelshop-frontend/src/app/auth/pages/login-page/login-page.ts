import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthStateService } from '../../services/auth-state.service';

interface LoginFormModel {
  email: FormControl<string>;
  password: FormControl<string>;
}

@Component({
  selector: 'app-login-page',
  imports: [ReactiveFormsModule],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginPage {
  private readonly authStateService = inject(AuthStateService);
  private readonly router = inject(Router);

  protected readonly isSubmitting = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  protected readonly form = new FormGroup<LoginFormModel>({
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(6)]
    })
  });

  constructor() {
    this.authStateService.restoreSession();
  }

  protected submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.errorMessage.set(null);
    this.isSubmitting.set(true);

    const { email, password } = this.form.getRawValue();

    this.authStateService.login({ email, password }).subscribe((isSuccessful) => {
      this.isSubmitting.set(false);

      if (!isSuccessful) {
        this.errorMessage.set('Login fehlgeschlagen. Pruefe E-Mail und Passwort.');
        return;
      }

      void this.router.navigate(['/']);
    });
  }

  protected emailHasError(): boolean {
    const control = this.form.controls.email;
    return control.invalid && (control.touched || control.dirty);
  }

  protected passwordHasError(): boolean {
    const control = this.form.controls.password;
    return control.invalid && (control.touched || control.dirty);
  }
}

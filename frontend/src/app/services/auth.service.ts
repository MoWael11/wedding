import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { SignInRequest, SignUpRequest, AuthResponse } from '../models';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USERNAME_KEY = 'auth_username';
  private readonly USERID_KEY = 'auth_userid';

  isAuthenticated = signal(this.hasToken());
  username = signal(this.getStoredUsername());
  userId = signal(this.getStoredUserId());

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  signIn(request: SignInRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/sign-in`, request).pipe(
      tap((response) => this.handleAuthSuccess(response)),
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  signUp(request: SignUpRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/sign-up`, request).pipe(
      tap((response) => this.handleAuthSuccess(response)),
      catchError((error) => {
        return throwError(() => error);
      })
    );
  }

  signOut(): void {
    const token = this.getToken();
    if (token) {
      this.http.post(`${this.API_URL}/sign-out`, {}).subscribe({
        complete: () => this.clearAuth(),
        error: () => this.clearAuth(),
      });
    } else {
      this.clearAuth();
    }
  }

  getToken(): string | null {
    return this.getCookie(this.TOKEN_KEY);
  }

  private handleAuthSuccess(response: AuthResponse): void {
    this.setCookie(this.TOKEN_KEY, response.token, 7);
    this.setCookie(this.USERNAME_KEY, response.username, 7);
    this.setCookie(this.USERID_KEY, response.userId.toString(), 7);
    this.isAuthenticated.set(true);
    this.username.set(response.username);
    this.userId.set(response.userId);
  }

  private clearAuth(): void {
    this.deleteCookie(this.TOKEN_KEY);
    this.deleteCookie(this.USERNAME_KEY);
    this.deleteCookie(this.USERID_KEY);
    this.isAuthenticated.set(false);
    this.username.set(null);
    this.userId.set(null);
    this.router.navigate(['/auth/sign-in']);
  }

  private hasToken(): boolean {
    return !!this.getCookie(this.TOKEN_KEY);
  }

  private getStoredUsername(): string | null {
    return this.getCookie(this.USERNAME_KEY);
  }

  private getStoredUserId(): number | null {
    const id = this.getCookie(this.USERID_KEY);
    return id ? parseInt(id, 10) : null;
  }

  private setCookie(name: string, value: string, days: number): void {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/;SameSite=Strict`;
  }

  private getCookie(name: string): string | null {
    const nameEQ = name + '=';
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
      let c = cookie.trim();
      if (c.indexOf(nameEQ) === 0) {
        return c.substring(nameEQ.length);
      }
    }
    return null;
  }

  private deleteCookie(name: string): void {
    document.cookie = `${name}=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/`;
  }
}

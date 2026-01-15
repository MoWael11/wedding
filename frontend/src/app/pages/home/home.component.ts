import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { WeddingService } from '../../services/wedding.service';
import { Wedding, CreateWeddingRequest } from '../../models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  weddings = signal<Wedding[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  // Modals
  showCreateModal = signal(false);
  showCodeModal = signal(false);

  // Create form
  createForm = {
    title: '',
    eventDate: '',
    person1FirstName: '',
    person1LastName: '',
    person2FirstName: '',
    person2LastName: '',
  };
  createLoading = signal(false);
  createError = signal<string | null>(null);

  // Code form
  codeInput = '';
  codeLoading = signal(false);
  codeError = signal<string | null>(null);

  constructor(
    public authService: AuthService,
    private weddingService: WeddingService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadWeddings();
  }

  loadWeddings(): void {
    this.loading.set(true);
    this.weddingService.getAll().subscribe({
      next: (weddings) => {
        this.weddings.set(weddings);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Failed to load weddings');
        this.loading.set(false);
      },
    });
  }

  openCreateModal(): void {
    this.showCreateModal.set(true);
    this.createError.set(null);
  }

  closeCreateModal(): void {
    this.showCreateModal.set(false);
    this.resetCreateForm();
  }

  resetCreateForm(): void {
    this.createForm = {
      title: '',
      eventDate: '',
      person1FirstName: '',
      person1LastName: '',
      person2FirstName: '',
      person2LastName: '',
    };
  }

  onCreateWedding(): void {
    if (
      !this.createForm.title ||
      !this.createForm.person1FirstName ||
      !this.createForm.person1LastName ||
      !this.createForm.person2FirstName ||
      !this.createForm.person2LastName
    ) {
      this.createError.set('Please fill in all required fields');
      return;
    }

    const request: CreateWeddingRequest = {
      title: this.createForm.title,
      eventDate: this.createForm.eventDate || undefined,
      person1: {
        firstName: this.createForm.person1FirstName,
        lastName: this.createForm.person1LastName,
      },
      person2: {
        firstName: this.createForm.person2FirstName,
        lastName: this.createForm.person2LastName,
      },
    };

    this.createLoading.set(true);
    this.createError.set(null);

    this.weddingService.create(request).subscribe({
      next: (wedding) => {
        this.createLoading.set(false);
        this.closeCreateModal();
        this.loadWeddings();
      },
      error: (err) => {
        this.createLoading.set(false);
        this.createError.set(err.error?.message || 'Failed to create wedding');
      },
    });
  }

  openCodeModal(): void {
    this.showCodeModal.set(true);
    this.codeError.set(null);
    this.codeInput = '';
  }

  closeCodeModal(): void {
    this.showCodeModal.set(false);
  }

  onEnterCode(): void {
    if (!this.codeInput.trim()) {
      this.codeError.set('Please enter a wedding code');
      return;
    }

    this.codeLoading.set(true);
    this.codeError.set(null);

    this.weddingService.getByCode(this.codeInput.trim()).subscribe({
      next: (wedding) => {
        this.codeLoading.set(false);
        this.closeCodeModal();
        this.router.navigate(['/weddings', wedding.id, 'upload']);
      },
      error: () => {
        this.codeLoading.set(false);
        this.codeError.set('Invalid wedding code');
      },
    });
  }

  onSignOut(): void {
    this.authService.signOut();
  }
}


import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { WeddingService } from '../../services/wedding.service';
import { Wedding } from '../../models';
import { CreateWeddingModalComponent, EnterCodeModalComponent } from '../../components';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, CreateWeddingModalComponent, EnterCodeModalComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  weddings = signal<Wedding[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  showCreateModal = signal(false);
  showCodeModal = signal(false);

  constructor(
    public authService: AuthService,
    private weddingService: WeddingService
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
      error: () => {
        this.error.set('Failed to load weddings');
        this.loading.set(false);
      },
    });
  }

  openCreateModal(): void {
    this.showCreateModal.set(true);
  }

  closeCreateModal(): void {
    this.showCreateModal.set(false);
  }

  onWeddingCreated(): void {
    this.loadWeddings();
  }

  openCodeModal(): void {
    this.showCodeModal.set(true);
  }

  closeCodeModal(): void {
    this.showCodeModal.set(false);
  }

  onSignOut(): void {
    this.authService.signOut();
  }
}


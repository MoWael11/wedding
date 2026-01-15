import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WeddingService } from '../../services/wedding.service';

@Component({
  selector: 'app-enter-code-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './enter-code-modal.component.html',
  styleUrl: './enter-code-modal.component.css',
})
export class EnterCodeModalComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();

  code = '';
  loading = signal(false);
  error = signal<string | null>(null);

  constructor(
    private weddingService: WeddingService,
    private router: Router
  ) {}

  onClose(): void {
    this.code = '';
    this.error.set(null);
    this.close.emit();
  }

  onOverlayClick(): void {
    this.onClose();
  }

  onSubmit(): void {
    if (!this.code.trim()) {
      this.error.set('Please enter a wedding code');
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.weddingService.getByCode(this.code.trim()).subscribe({
      next: (wedding) => {
        this.loading.set(false);
        this.onClose();
        this.router.navigate(['/weddings', wedding.id, 'upload']);
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Invalid wedding code');
      },
    });
  }
}

import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CreateWeddingRequest } from '../../models';
import { WeddingService } from '../../services/wedding.service';

@Component({
  selector: 'app-create-wedding-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-wedding-modal.component.html',
  styleUrl: './create-wedding-modal.component.css',
})
export class CreateWeddingModalComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  @Output() created = new EventEmitter<void>();

  form = {
    title: '',
    eventDate: '',
    person1FirstName: '',
    person1LastName: '',
    person2FirstName: '',
    person2LastName: '',
  };

  loading = signal(false);
  error = signal<string | null>(null);

  constructor(private weddingService: WeddingService) {}

  onClose(): void {
    this.resetForm();
    this.close.emit();
  }

  onOverlayClick(): void {
    this.onClose();
  }

  onSubmit(): void {
    if (
      !this.form.title ||
      !this.form.person1FirstName ||
      !this.form.person1LastName ||
      !this.form.person2FirstName ||
      !this.form.person2LastName
    ) {
      this.error.set('Please fill in all required fields');
      return;
    }

    const request: CreateWeddingRequest = {
      title: this.form.title,
      eventDate: this.form.eventDate || undefined,
      person1: {
        firstName: this.form.person1FirstName,
        lastName: this.form.person1LastName,
      },
      person2: {
        firstName: this.form.person2FirstName,
        lastName: this.form.person2LastName,
      },
    };

    this.loading.set(true);
    this.error.set(null);

    this.weddingService.create(request).subscribe({
      next: () => {
        this.loading.set(false);
        this.resetForm();
        this.created.emit();
        this.close.emit();
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err.error?.message || 'Failed to create wedding');
      },
    });
  }

  private resetForm(): void {
    this.form = {
      title: '',
      eventDate: '',
      person1FirstName: '',
      person1LastName: '',
      person2FirstName: '',
      person2LastName: '',
    };
    this.error.set(null);
  }
}

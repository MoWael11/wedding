import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { WeddingService } from '../../services/wedding.service';
import { Wedding } from '../../models';

@Component({
  selector: 'app-wedding-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wedding-upload.component.html',
  styleUrl: './wedding-upload.component.css',
})
export class WeddingUploadComponent implements OnInit {
  wedding = signal<Wedding | null>(null);
  loading = signal(true);
  uploading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  selectedFile = signal<File | null>(null);
  previewUrl = signal<string | null>(null);

  private weddingId: number = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private weddingService: WeddingService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.weddingId = +id;
      this.loadWedding(this.weddingId);
    }
  }

  loadWedding(id: number): void {
    this.weddingService.getById(id).subscribe({
      next: (wedding) => {
        this.wedding.set(wedding);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Wedding not found');
        this.loading.set(false);
      },
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      if (!['image/jpeg', 'image/png'].includes(file.type)) {
        this.error.set('Only JPG and PNG images are allowed');
        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        this.error.set('File size must be less than 5MB');
        return;
      }

      this.error.set(null);
      this.selectedFile.set(file);

      const reader = new FileReader();
      reader.onload = () => {
        this.previewUrl.set(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  }

  clearFile(): void {
    this.selectedFile.set(null);
    this.previewUrl.set(null);
  }

  uploadImage(): void {
    const file = this.selectedFile();
    if (!file) return;

    this.uploading.set(true);
    this.error.set(null);

    this.weddingService.uploadImage(this.weddingId, file).subscribe({
      next: () => {
        this.success.set('Image uploaded successfully!');
        this.uploading.set(false);
        
        setTimeout(() => {
          this.router.navigate(['/weddings', this.weddingId]);
        }, 1500);
      },
      error: (err) => {
        this.uploading.set(false);
        this.error.set(err.error?.message || 'Failed to upload image');
      },
    });
  }

  goToWedding(): void {
    this.router.navigate(['/weddings', this.weddingId]);
  }
}

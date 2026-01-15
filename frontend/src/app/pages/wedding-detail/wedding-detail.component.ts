import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { WeddingService } from '../../services/wedding.service';
import { Wedding, Image } from '../../models';

@Component({
  selector: 'app-wedding-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './wedding-detail.component.html',
  styleUrl: './wedding-detail.component.css',
})
export class WeddingDetailComponent implements OnInit {
  wedding = signal<Wedding | null>(null);
  images = signal<Image[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  // check if current user is owner
  isOwner = computed(() => {
    const w = this.wedding();
    const currentUserId = this.authService.userId();
    if (!w || !currentUserId) return false;
    return w.ownerId === currentUserId;
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private weddingService: WeddingService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadWedding(+id);
    }
  }

  loadWedding(id: number): void {
    this.loading.set(true);

    this.weddingService.getById(id).subscribe({
      next: (wedding) => {
        this.wedding.set(wedding);
        this.loadImages(id);
      },
      error: () => {
        this.error.set('Wedding not found');
        this.loading.set(false);
      },
    });
  }

  loadImages(weddingId: number): void {
    this.weddingService.getImages(weddingId).subscribe({
      next: (images) => {
        this.images.set(images);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}

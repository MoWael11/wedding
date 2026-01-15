import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Wedding, CreateWeddingRequest, Image } from '../models';

@Injectable({
  providedIn: 'root',
})
export class WeddingService {
  private readonly API_URL = `${environment.apiUrl}/weddings`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Wedding[]> {
    return this.http.get<Wedding[]>(this.API_URL);
  }

  getById(id: number): Observable<Wedding> {
    return this.http.get<Wedding>(`${this.API_URL}/${id}`);
  }

  getByCode(code: string): Observable<Wedding> {
    return this.http.get<Wedding>(`${this.API_URL}/code/${code}`);
  }

  create(request: CreateWeddingRequest): Observable<Wedding> {
    return this.http.post<Wedding>(this.API_URL, request);
  }

  getImages(weddingId: number): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.API_URL}/${weddingId}/images`);
  }

  uploadImage(weddingId: number, file: File): Observable<Image> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Image>(`${this.API_URL}/${weddingId}/images`, formData);
  }
}

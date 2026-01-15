export interface Person {
  id: number;
  firstName: string;
  lastName: string;
}

export interface Wedding {
  id: number;
  code: string;
  title: string;
  eventDate: string | null;
  totalBytes: number;
  ownerId: number;
  persons: Person[];
  createdAt: string;
}

export interface CreateWeddingRequest {
  title: string;
  eventDate?: string;
  person1: {
    firstName: string;
    lastName: string;
  };
  person2: {
    firstName: string;
    lastName: string;
  };
}

export interface Image {
  id: number;
  originalFilename: string;
  url: string;
  mimeType: string;
  sizeBytes: number;
  uploadedAt: string;
  uploadedBy: string;
}

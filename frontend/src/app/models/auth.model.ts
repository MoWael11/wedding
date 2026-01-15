export interface SignInRequest {
  username: string;
  password: string;
}

export interface SignUpRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  token: string;
  username: string;
  role: string;
}

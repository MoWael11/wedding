# Wedding App - Frontend

Angular application for the Wedding photo sharing platform.

## Tech Stack

- **Angular 21**
- **TypeScript**
- **Standalone Components**
- **Signals** (reactive state management)
- **Cookie-based Authentication**

## Prerequisites

- Node.js 18+
- npm or yarn

## Getting Started

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure Environment

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

### 3. Start Development Server

```bash
ng serve
```

The app will be available at `http://localhost:4200`

## Features

- **User Authentication** - Sign up, sign in, sign out
- **Wedding Gallery** - View all weddings on the home page
- **Create Wedding** - Create a new wedding event
- **Enter with Code** - Join a wedding using a secret code
- **Upload Photos** - Share photos with wedding guests
- **Owner Dashboard** - View secret code (owners only)

## Project Structure

```
src/app/
├── guards/              # Route guards
│   └── auth.guard.ts    # Protects authenticated routes
├── interceptors/        # HTTP interceptors
│   └── auth.interceptor.ts  # Adds credentials to requests
├── models/              # TypeScript interfaces
│   ├── auth.model.ts    # Auth DTOs
│   └── wedding.model.ts # Wedding DTOs
├── pages/               # Page components
│   ├── auth/
│   │   ├── sign-in/     # Login page
│   │   └── sign-up/     # Registration page
│   ├── home/            # Wedding gallery
│   ├── wedding-detail/  # Wedding info & photos
│   └── wedding-upload/  # Photo upload page
├── services/            # API services
│   ├── auth.service.ts  # Authentication
│   └── wedding.service.ts # Wedding operations
├── app.config.ts        # App configuration
└── app.routes.ts        # Routing
```

## Authentication Flow

1. User signs in → receives token
2. Token stored in cookies (7 days)
3. `AuthInterceptor` adds `withCredentials: true` to all requests
4. `AuthGuard` protects routes requiring authentication

## Development Commands

```bash
# Start dev server
ng serve

# Build for production
ng build

# Run tests
ng test

# Lint code
ng lint
```

## Environment Variables

| Variable | Description |
|----------|-------------|
| `apiUrl` | Backend API URL |

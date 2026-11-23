# JobTrackr frontend

React + TypeScript + Vite frontend for the existing JobTrackr Spring Boot REST API.

## Requirements
- Node.js 20.19+ or 22.12+
- Backend running at http://localhost:8080

## Setup

```powershell
cd frontend
npm install
Copy-Item .env.example .env
npm run dev
```

Open http://localhost:5173.

To build for production:

```powershell
npm run build
```

## API configuration
The default API base URL is `http://localhost:8080/api`. Override it in `.env` with `VITE_API_BASE_URL` if needed.

## Backend CORS
If the browser reports a CORS error, allow `http://localhost:5173` in the Spring Boot backend CORS configuration. Do not change the database configuration.

## Implemented screens
- Dashboard with application pipeline, company breakdown, upcoming interviews/follow-ups, and recent activity
- Applications: create, list, update status, delete
- Companies: create, list, delete
- HR contacts: create, list, delete
- Interviews: create, list, update result, delete
- Offers: create, list, update status, delete
- Follow-ups: create, list, update status, delete
- Activity timeline: view recorded activities

The forms use the request DTOs and endpoint paths from the supplied backend project. This is a single-user application and intentionally has no authentication.

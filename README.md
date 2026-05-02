# Family Wealth Tracker

A full-stack web application to track family assets, visualize true allocation, and keep investments updated with stale-aware price syncing.

## Features

- Portfolio dashboard with assets and holdings.
- Asset tracking for stocks, mutual funds, crypto, real estate, FDs, cash, and other assets.
- Look-through allocation buckets: large cap, mid cap, small cap, foreign, crypto, real estate, debt/cash, and other.
- Price cache with manual refresh and scheduled stale refresh.
- Interactive allocation charts with Recharts.

## Tech Stack

- Frontend: Next.js, React, Recharts, Axios, lucide-react.
- Backend: Spring Boot, JPA/Hibernate, REST APIs.
- Database: PostgreSQL for deployment, H2 for local backend-only development.
- Infra: Docker Compose for local full-stack runs.

## Project Structure

```text
family-wealth-tracker/
├── backend/
├── frontend/
├── docker-compose.yml
└── README.md
```

## Local Development

### Backend

```bash
cd backend
mvn spring-boot:run
```

By default the backend uses an in-memory H2 database with sample data. For PostgreSQL or Supabase, set:

```bash
export DATABASE_URL=jdbc:postgresql://host:5432/database
export DATABASE_USERNAME=your_user
export DATABASE_PASSWORD=your_password
```

To use Supabase without committing credentials, create `backend/.env` from the tracked example:

```bash
cp backend/.env.example backend/.env
```

Then put your real Supabase values in `backend/.env`. This file is ignored by Git. The backend imports it automatically when you run from `backend/`:

```bash
cd backend
./mvnw spring-boot:run
```

Supabase usually provides a pooled PostgreSQL URL. Use JDBC format and keep the password in `DATABASE_PASSWORD`, for example:

```text
DATABASE_URL=jdbc:postgresql://aws-0-region.pooler.supabase.com:6543/postgres?sslmode=require&prepareThreshold=0
DATABASE_USERNAME=postgres.your-project-ref
DATABASE_PASSWORD=your_real_password
HIBERNATE_DDL_AUTO=validate
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend expects the API at `http://localhost:8080/api`. Override it with:

```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

### Docker

```bash
docker-compose up --build
```

Then open:

- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080/api/dashboard`

## Price Syncing

Market-priced assets use the cached price when available and fall back to the manually entered price otherwise.

| Asset Type | Source | Stale After |
| --- | --- | --- |
| Stocks | Yahoo Finance chart endpoint | 1 hour |
| Crypto | CoinGecko simple price | 15 minutes |
| Mutual funds | AMFI NAV via mfapi.in | 1 day |

Manual refresh:

```bash
curl -X POST "http://localhost:8080/api/prices/refresh?force=true"
```

## API Overview

- `GET /api/dashboard`
- `GET /api/assets`
- `POST /api/assets`
- `PUT /api/assets/{id}`
- `DELETE /api/assets/{id}`
- `POST /api/prices/refresh?force=true`

## Roadmap

- CAS PDF import.
- AI-assisted asset entry.
- Goal tracking.
- Multi-user family accounts.
- Mobile app.

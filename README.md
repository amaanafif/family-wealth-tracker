# 💰 Net Worth Tracker

A full-stack web application to track **family net worth**, visualize **true asset allocation**, and keep investments updated with **smart price syncing**.

---

## 🚀 Features

### 📊 Net Worth Dashboard

* Total net worth (assets - liabilities)
* Real-time breakdown across:

  * Equity
  * Foreign Equity
  * Crypto
  * Real Estate
  * Debt / Cash

---

### 🧠 Look-through Asset Allocation

* Converts all investments into:

  * Large Cap
  * Mid Cap
  * Small Cap
  * Foreign
* Mutual funds are **broken down internally** (no flexi/multi cap clutter)

---

### 💹 Price Updates (Smart + Free)

* Stocks → Yahoo Finance
* Crypto → CoinGecko
* Mutual Funds → AMFI NAV

✔ Cached in database
✔ Auto-refresh (daily)
✔ Manual refresh option
✔ No unnecessary API calls

---

### 🧾 Asset & Liability Tracking

* Add/edit:

  * Stocks, MFs, Crypto
  * Real estate, FDs, cash
  * Loans & liabilities

---

### 📈 Interactive Charts

* Portfolio allocation pie chart
* Drill-down into equity (large/mid/small)

---

## 🏗️ Tech Stack

### Frontend

* Next.js (React)
* Recharts
* Axios

### Backend

* Spring Boot (Java)
* JPA / Hibernate
* REST APIs

### Database

* PostgreSQL (Supabase)

### Infra

* Docker (optional)
* Vercel (frontend)
* Render / Railway (backend)

---

## 🧩 Architecture

```text
Frontend (Next.js)
        ↓
Spring Boot API
        ↓
PostgreSQL (Supabase)
        ↓
External APIs (Yahoo / CoinGecko / AMFI)
```

---

## 📂 Project Structure

```bash
networth-app/
├── backend/
├── frontend/
├── docker-compose.yml
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Clone repo

```bash
git clone https://github.com/your-username/networth-app.git
cd networth-app
```

---

### 2. Backend setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Configure DB in `application.yml`

---

### 3. Frontend setup

```bash
cd frontend
npm install
npm run dev
```

---

### 4. Run with Docker (optional)

```bash
docker-compose up
```

---

## 🔄 Price Update Strategy

* Prices are **cached in DB**
* Updated:

  * Automatically (daily scheduler)
  * On manual refresh
* Only refreshed if **data is stale**

| Asset Type | Update Frequency |
| ---------- | ---------------- |
| Stocks     | ~1 hour          |
| Crypto     | ~15 minutes      |
| MF NAV     | Daily            |

---

## ⚠️ Limitations

* Uses free APIs (may have rate limits)
* Not real-time tick data
* Designed for personal use / MVP

---

## 🛣️ Roadmap

* [ ] CAS PDF import (auto-detect MF + stocks)
* [ ] AI-based asset entry
* [ ] Goal tracking
* [ ] Multi-user family accounts
* [ ] Mobile app

---

## 🤝 Contributing

Pull requests are welcome.
For major changes, please open an issue first.

---

## 📜 License

MIT License

---

## 💡 Inspiration

Built to create a **single source of truth for personal and family wealth**, without relying on multiple apps.

---

## ⭐ If you like this project

Give it a star ⭐ — helps visibility!

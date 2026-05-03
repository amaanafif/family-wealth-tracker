"use client";

import { useCallback, useEffect, useState } from "react";
import {
  Banknote,
  Landmark,
  LayoutDashboard,
  Plus,
  RefreshCcw,
  Scale,
  WalletCards,
} from "lucide-react";
import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";
import { AssetForm } from "@/components/AssetForm";
import { HoldingsTable } from "@/components/HoldingsTable";
import { getAssets, getDashboard, refreshPrices } from "@/lib/api";
import { money, percent } from "@/lib/format";
import type { Asset, DashboardSummary } from "@/types/wealth";

export default function Home() {
  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null);
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const load = useCallback(async () => {
    const [summary, assetList] = await Promise.all([
      getDashboard(),
      getAssets(),
    ]);
    setDashboard(summary);
    setAssets(assetList);
    setLoading(false);
  }, []);

  useEffect(() => {
    load().catch(() => setLoading(false));
  }, [load]);

  async function syncPrices() {
    setRefreshing(true);
    await refreshPrices(true);
    await load();
    setRefreshing(false);
  }

  const colors = [
    "#0f766e",
    "#2563eb",
    "#ca8a04",
    "#be123c",
    "#15803d",
    "#7c3aed",
    "#475569",
  ];

  if (loading) {
    return <main className="page state-page">Loading wealth dashboard...</main>;
  }

  if (!dashboard) {
    return (
        <main className="page state-page">
          Start the Spring Boot API on port 8080, then refresh this page.
        </main>
    );
  }

  return (
      <main className="page">
        <header className="app-shell">
          <div className="brand-mark">
            <Landmark size={18} />
          </div>
          <div className="brand">
            <strong>Kin Wealth</strong>
            <span>Family net worth</span>
          </div>
          <nav className="nav-tabs">
            <button className="nav-tab active">
              <LayoutDashboard size={15} />
              Dashboard
            </button>
            <button className="nav-tab">
              <WalletCards size={15} />
              Assets
            </button>
          </nav>
        </header>

        {/* ===== HERO SECTION ===== */}
        <section className="hero-row">
          <div className="hero-metrics">
            {/* MAIN */}
            <div className="hero-main primary">
              <span className="eyebrow">Family overview</span>
              <h1>{money(dashboard.netWorth)}</h1>
              <p>Total portfolio value</p>
            </div>

            {/* INVESTED */}
            <div className="hero-main secondary">
              <span className="eyebrow">Invested value</span>
              <h2>{money(dashboard.totalInvested)}</h2>
            </div>

            {/* RETURNS */}
            <div className="hero-main secondary">
              <span className="eyebrow">Return till date</span>
              <h2
                  className={
                    dashboard.totalProfitLoss >= 0
                        ? "positive-value"
                        : "negative-value"
                  }
              >
                {dashboard.totalInvested > 0
                    ? percent(
                        (dashboard.totalProfitLoss /
                            dashboard.totalInvested) *
                        100
                    )
                    : percent(0)}
              </h2>
              <p>Profit & loss</p>
            </div>
          </div>

          <div className="hero-actions">
            <button
                className="secondary"
                disabled={refreshing}
                onClick={syncPrices}
            >
              <RefreshCcw size={17} />
              Refresh prices
            </button>
            <button>
              <Plus size={17} />
              Upload sta ̑tement
            </button>
          </div>
        </section>

        {/* ===== REST SAME ===== */}
        <section className="allocation-panel panel">
          <div className="panel-title">
            <div>
              <span className="eyebrow">Allocation</span>
              <h2>Portfolio Mix</h2>
              <p>Review asset type allocation.</p>
            </div>
          </div>

          <div className="allocation-content">
            <ResponsiveContainer width="100%" height={280}>
              <PieChart>
                <Pie
                    data={dashboard.allocationByType}
                    dataKey="value"
                    innerRadius={70}
                    outerRadius={110}
                >
                  {dashboard.allocationByType.map((entry, index) => (
                      <Cell
                          key={entry.label}
                          fill={colors[index % colors.length]}
                      />
                  ))}
                </Pie>
                <Tooltip formatter={(v: number) => money(v)} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </section>

        <section className="workspace">
          <div className="main-stack">
            <HoldingsTable holdings={dashboard.holdings} />
          </div>
          <aside>
            <AssetForm assets={assets} onChanged={load} />
          </aside>
        </section>
      </main>
  );
}
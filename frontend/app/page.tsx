"use client";

import { useCallback, useEffect, useState } from "react";
import { Banknote, Landmark, LayoutDashboard, Plus, RefreshCcw, Scale, TrendingUp, WalletCards } from "lucide-react";
import { AllocationCharts } from "@/components/AllocationCharts";
import { AssetForm } from "@/components/AssetForm";
import { HoldingsTable } from "@/components/HoldingsTable";
import { getAssets, getDashboard, refreshPrices } from "@/lib/api";
import { money } from "@/lib/format";
import type { Asset, DashboardSummary } from "@/types/wealth";

export default function Home() {
  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null);
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const load = useCallback(async () => {
    const [summary, assetList] = await Promise.all([getDashboard(), getAssets()]);
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

  if (loading) {
    return <main className="page state-page">Loading wealth dashboard...</main>;
  }

  if (!dashboard) {
    return <main className="page state-page">Start the Spring Boot API on port 8080, then refresh this page.</main>;
  }

  return (
    <main className="page">
      <header className="app-shell">
        <div className="brand-mark" aria-hidden="true">
          <Landmark size={18} />
        </div>
        <div className="brand">
          <strong>Kin Wealth</strong>
          <span>Family net worth</span>
        </div>
        <nav className="nav-tabs" aria-label="Primary navigation">
          <button className="nav-tab active" type="button">
            <LayoutDashboard size={15} />
            Dashboard
          </button>
          <button className="nav-tab" type="button">
            <WalletCards size={15} />
            Assets
          </button>
        </nav>
      </header>

      <section className="hero-row">
        <div>
          <span className="eyebrow">Family overview</span>
          <h1>{money(dashboard.netWorth)}</h1>
          <p>Total portfolio value with look-through allocation.</p>
        </div>
        <div className="hero-actions">
          <button className="secondary" disabled={refreshing} type="button" onClick={syncPrices}>
            <RefreshCcw size={17} />
            Refresh prices
          </button>
          <button type="button">
            <Plus size={17} />
            Add asset
          </button>
        </div>
      </section>

      <section className="summary-grid two-up">
        <div className="metric">
          <div className="metric-heading">
            <span>Total assets</span>
            <WalletCards size={17} />
          </div>
          <strong>{money(dashboard.totalAssets)}</strong>
          <small>{dashboard.holdings.length} holdings</small>
        </div>
        <div className="metric">
          <div className="metric-heading growth-accent">
            <span>Portfolio value</span>
            <TrendingUp size={17} />
          </div>
          <strong>{money(dashboard.netWorth)}</strong>
          <small>Current tracked asset value</small>
        </div>
      </section>

      <section className="allocation-panel panel">
        <div className="panel-title">
          <div>
            <span className="eyebrow">Allocation</span>
            <h2>Portfolio Mix</h2>
            <p>Review asset type and look-through exposure.</p>
          </div>
          <button className="toggle-pill" type="button" aria-label="Allocation view enabled">
            <span />
          </button>
        </div>
        <div className="charts">
          <AllocationCharts title="Asset Allocation" data={dashboard.allocationByType} />
          <AllocationCharts title="Look-through Equity" data={dashboard.lookThroughAllocation} />
        </div>
      </section>

      <section className="workspace">
        <div className="main-stack">
          <HoldingsTable holdings={dashboard.holdings} />
          <div className="insight-grid">
            <div className="panel assistant-panel">
              <span className="eyebrow">AI assistant</span>
              <h2>Add investments in plain English</h2>
              <p>Describe what you hold and keep the portfolio structured.</p>
              <textarea readOnly value={'e.g. "I invested 2 lakh in HDFC Mid Cap Fund and 50k in Bitcoin"'} />
              <button className="soft-action" type="button">
                <Banknote size={16} />
                Parse with AI
              </button>
            </div>
            <div className="panel insight-panel">
              <span className="eyebrow">Insights</span>
              <h2>Explain my portfolio</h2>
              <p>Commentary on diversification, concentration, and liquidity.</p>
              <button type="button">
                <Scale size={16} />
                Generate
              </button>
            </div>
          </div>
        </div>
        <aside>
          <AssetForm assets={assets} onChanged={load} />
        </aside>
      </section>
    </main>
  );
}

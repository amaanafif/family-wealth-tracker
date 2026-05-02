"use client";

import { useCallback, useEffect, useState } from "react";
import { RefreshCcw } from "lucide-react";
import { AllocationCharts } from "@/components/AllocationCharts";
import { AssetForm } from "@/components/AssetForm";
import { HoldingsTable } from "@/components/HoldingsTable";
import { LiabilityForm } from "@/components/LiabilityForm";
import { getAssets, getDashboard, getLiabilities, refreshPrices } from "@/lib/api";
import { money } from "@/lib/format";
import type { Asset, DashboardSummary, Liability } from "@/types/wealth";

export default function Home() {
  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null);
  const [assets, setAssets] = useState<Asset[]>([]);
  const [liabilities, setLiabilities] = useState<Liability[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const load = useCallback(async () => {
    const [summary, assetList, liabilityList] = await Promise.all([getDashboard(), getAssets(), getLiabilities()]);
    setDashboard(summary);
    setAssets(assetList);
    setLiabilities(liabilityList);
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
    return <main className="page">Loading wealth dashboard...</main>;
  }

  if (!dashboard) {
    return <main className="page">Start the Spring Boot API on port 8080, then refresh this page.</main>;
  }

  return (
    <main className="page">
      <div className="topbar">
        <div className="brand">
          <h1>Family Wealth Tracker</h1>
          <p>Net worth, liabilities, true allocation, and stale-aware market price refreshes in one focused workspace.</p>
        </div>
        <button className="secondary" disabled={refreshing} type="button" onClick={syncPrices}>
          <RefreshCcw size={18} />
          Refresh prices
        </button>
      </div>

      <section className="summary-grid">
        <div className="metric">
          <span>Net worth</span>
          <strong>{money(dashboard.netWorth)}</strong>
        </div>
        <div className="metric">
          <span>Total assets</span>
          <strong>{money(dashboard.totalAssets)}</strong>
        </div>
        <div className="metric">
          <span>Total liabilities</span>
          <strong>{money(dashboard.totalLiabilities)}</strong>
        </div>
      </section>

      <section className="workspace">
        <div>
          <div className="charts">
            <AllocationCharts title="Asset Allocation" data={dashboard.allocationByType} />
            <AllocationCharts title="Look-through Equity" data={dashboard.lookThroughAllocation} />
          </div>
          <HoldingsTable holdings={dashboard.holdings} />
        </div>
        <aside>
          <AssetForm assets={assets} onChanged={load} />
          <LiabilityForm liabilities={liabilities} onChanged={load} />
        </aside>
      </section>
    </main>
  );
}

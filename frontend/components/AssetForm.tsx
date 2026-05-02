"use client";

import { FormEvent, useState } from "react";
import { Plus, Trash2 } from "lucide-react";
import { createAsset, deleteAsset } from "@/lib/api";
import { label, money } from "@/lib/format";
import type { AllocationBucket, Asset, AssetType } from "@/types/wealth";

const assetTypes: AssetType[] = ["STOCK", "MF", "CRYPTO", "REAL_ESTATE", "FD", "CASH"];
const buckets: AllocationBucket[] = ["LARGE_CAP", "MID_CAP", "SMALL_CAP", "FOREIGN", "CRYPTO", "REAL_ESTATE", "DEBT_CASH", "OTHER"];

type Props = {
  assets: Asset[];
  onChanged: () => void;
};

const initial: Asset = {
  name: "",
  symbol: "",
  type: "STOCK",
  bucket: "LARGE_CAP",
  quantity: 1,
  manualPrice: 0,
};

export function AssetForm({ assets, onChanged }: Props) {
  const [asset, setAsset] = useState<Asset>(initial);
  const [saving, setSaving] = useState(false);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    await createAsset({
      ...asset,
      symbol: asset.symbol || null,
      quantity: Number(asset.quantity),
      manualPrice: Number(asset.manualPrice),
    });
    setAsset(initial);
    setSaving(false);
    onChanged();
  }

  async function remove(id?: string) {
    if (!id) return;
    await deleteAsset(id);
    onChanged();
  }

  return (
    <div className="panel">
      <div className="panel-title">
        <h2>Assets</h2>
      </div>
      <form onSubmit={submit}>
        <div className="form-grid">
          <div className="field">
            <label>Name</label>
            <input required value={asset.name} onChange={(event) => setAsset({ ...asset, name: event.target.value })} />
          </div>
          <div className="field">
            <label>Symbol</label>
            <input value={asset.symbol ?? ""} onChange={(event) => setAsset({ ...asset, symbol: event.target.value })} />
          </div>
          <div className="field">
            <label>Type</label>
            <select value={asset.type} onChange={(event) => setAsset({ ...asset, type: event.target.value as AssetType })}>
              {assetTypes.map((type) => (
                <option key={type} value={type}>
                  {label(type)}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Look-through bucket</label>
            <select value={asset.bucket} onChange={(event) => setAsset({ ...asset, bucket: event.target.value as AllocationBucket })}>
              {buckets.map((bucket) => (
                <option key={bucket} value={bucket}>
                  {label(bucket)}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Quantity</label>
            <input min="0" step="0.0001" type="number" value={asset.quantity} onChange={(event) => setAsset({ ...asset, quantity: Number(event.target.value) })} />
          </div>
          <div className="field">
            <label>Fallback price</label>
            <input min="0" step="0.01" type="number" value={asset.manualPrice} onChange={(event) => setAsset({ ...asset, manualPrice: Number(event.target.value) })} />
          </div>
        </div>
        <div className="actions">
          <button disabled={saving} type="submit">
            <Plus size={18} />
            Add asset
          </button>
        </div>
      </form>
      <div className="list">
        {assets.map((item) => (
          <div className="row" key={item.id}>
            <div>
              <strong>{item.name}</strong>
              <small>
                {label(item.type)} · {item.quantity} units · {money(item.manualPrice)}
              </small>
            </div>
            <button className="danger" type="button" aria-label={`Delete ${item.name}`} onClick={() => remove(item.id)}>
              <Trash2 size={17} />
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

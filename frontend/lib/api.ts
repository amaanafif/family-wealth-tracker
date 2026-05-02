import axios from "axios";
import type { Asset, DashboardSummary } from "@/types/wealth";

export const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api",
});

export async function getDashboard() {
  const { data } = await api.get<DashboardSummary>("/dashboard");
  return data;
}

export async function getAssets() {
  const { data } = await api.get<Asset[]>("/assets");
  return data;
}

export async function createAsset(asset: Asset) {
  const { data } = await api.post<Asset>("/assets", asset);
  return data;
}

export async function deleteAsset(id: string) {
  await api.delete(`/assets/${id}`);
}

export async function refreshPrices(force = false) {
  const { data } = await api.post<{ refreshed: number; skipped: number }>(`/prices/refresh?force=${force}`);
  return data;
}

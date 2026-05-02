import axios from "axios";
import type { Asset, DashboardSummary, Liability } from "@/types/wealth";

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

export async function deleteAsset(id: number) {
  await api.delete(`/assets/${id}`);
}

export async function getLiabilities() {
  const { data } = await api.get<Liability[]>("/liabilities");
  return data;
}

export async function createLiability(liability: Liability) {
  const { data } = await api.post<Liability>("/liabilities", liability);
  return data;
}

export async function deleteLiability(id: number) {
  await api.delete(`/liabilities/${id}`);
}

export async function refreshPrices(force = false) {
  const { data } = await api.post<{ refreshed: number; skipped: number }>(`/prices/refresh?force=${force}`);
  return data;
}

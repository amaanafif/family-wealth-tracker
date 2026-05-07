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

// Import assets from CSV/Excel file
export async function importAssets(file: File) {
  const formData = new FormData();
  formData.append("file", file);
  try {
    console.log("Uploading file:", file.name, "Size:", file.size);
    const { data } = await api.post("/assets/import", formData);
    console.log("Upload response:", data);
    return data;
  } catch (error) {
    console.error("Upload failed:", error);
    throw error;
  }
}


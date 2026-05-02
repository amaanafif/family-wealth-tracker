export type AssetType =
  | "STOCK"
  | "MF"
  | "CRYPTO"
  | "REAL_ESTATE"
  | "FD"
  | "CASH";

export type AllocationBucket =
  | "LARGE_CAP"
  | "MID_CAP"
  | "SMALL_CAP"
  | "FOREIGN"
  | "CRYPTO"
  | "REAL_ESTATE"
  | "DEBT_CASH"
  | "OTHER";

export type Asset = {
  id?: string;
  name: string;
  symbol?: string | null;
  type: AssetType;
  bucket: AllocationBucket;
  quantity: number;
  manualPrice: number;
};

export type AllocationSlice = {
  label: string;
  value: number;
  percentage: number;
};

export type Holding = {
  id: string;
  name: string;
  type: AssetType;
  bucket: AllocationBucket;
  quantity: number;
  price: number;
  value: number;
};

export type DashboardSummary = {
  totalAssets: number;
  netWorth: number;
  allocationByType: AllocationSlice[];
  lookThroughAllocation: AllocationSlice[];
  holdings: Holding[];
};

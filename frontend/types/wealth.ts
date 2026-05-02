export type AssetType =
  | "STOCK"
  | "MUTUAL_FUND"
  | "CRYPTO"
  | "REAL_ESTATE"
  | "FIXED_DEPOSIT"
  | "CASH"
  | "OTHER";

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
  id?: number;
  name: string;
  symbol?: string | null;
  type: AssetType;
  bucket: AllocationBucket;
  quantity: number;
  manualPrice: number;
};

export type Liability = {
  id?: number;
  name: string;
  outstandingAmount: number;
  interestRate?: number | null;
};

export type AllocationSlice = {
  label: string;
  value: number;
  percentage: number;
};

export type Holding = {
  id: number;
  name: string;
  type: AssetType;
  bucket: AllocationBucket;
  quantity: number;
  price: number;
  value: number;
};

export type DashboardSummary = {
  totalAssets: number;
  totalLiabilities: number;
  netWorth: number;
  allocationByType: AllocationSlice[];
  lookThroughAllocation: AllocationSlice[];
  holdings: Holding[];
};

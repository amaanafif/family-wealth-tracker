export function money(value: number) {
  return new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(value || 0);
}

export function percent(value: number) {
  return `${Number(value || 0).toFixed(1)}%`;
}

export function label(value: string) {
  if (value === "MF") {
    return "Mutual Fund";
  }
  if (value === "FD") {
    return "Fixed Deposit";
  }
  return value
    .toLowerCase()
    .split("_")
    .map((word) => word[0].toUpperCase() + word.slice(1))
    .join(" ");
}

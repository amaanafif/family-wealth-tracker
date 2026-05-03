import type { Holding } from "@/types/wealth";
import { label, money } from "@/lib/format";

export function HoldingsTable({ holdings }: { holdings: Holding[] }) {
  return (
    <div className="panel">
      <div className="panel-title">
        <h2>Holdings</h2>
      </div>
      <table className="holdings">
        <thead>
          <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Bucket</th>
            <th>Invested</th>
            <th>Current Value</th>
            <th>Profit/Loss</th>
          </tr>
        </thead>
        <tbody>
          {holdings.map((holding) => (
            <tr key={holding.id}>
              <td>{holding.name}</td>
              <td>{label(holding.type)}</td>
              <td>{label(holding.bucket)}</td>
              <td>{money(holding.investedValue)}</td>
              <td>{money(holding.value)}</td>
              <td className={holding.profitLoss >= 0 ? "positive-value" : "negative-value"}>
                {holding.profitLoss >= 0 ? "+" : ""}{money(holding.profitLoss)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

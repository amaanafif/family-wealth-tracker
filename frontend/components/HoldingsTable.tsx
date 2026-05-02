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
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          {holdings.map((holding) => (
            <tr key={holding.id}>
              <td>{holding.name}</td>
              <td>{label(holding.type)}</td>
              <td>{label(holding.bucket)}</td>
              <td>{money(holding.value)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

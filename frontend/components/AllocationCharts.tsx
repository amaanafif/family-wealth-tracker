"use client";

import { Cell, Pie, PieChart, ResponsiveContainer, Tooltip } from "recharts";
import type { AllocationSlice } from "@/types/wealth";
import { money, percent } from "@/lib/format";

const colors = ["#0f766e", "#2563eb", "#ca8a04", "#be123c", "#15803d", "#7c3aed", "#475569"];

type Props = {
  title: string;
  data: AllocationSlice[];
};

export function AllocationCharts({ title, data }: Props) {
  return (
    <div className="panel chart-box">
      <div className="panel-title">
        <h2>{title}</h2>
      </div>
      <ResponsiveContainer width="100%" height={235}>
        <PieChart>
          <Pie data={data} dataKey="value" nameKey="label" innerRadius={58} outerRadius={92} paddingAngle={2}>
            {data.map((entry, index) => (
              <Cell key={entry.label} fill={colors[index % colors.length]} />
            ))}
          </Pie>
          <Tooltip formatter={(value: number) => money(value)} />
        </PieChart>
      </ResponsiveContainer>
      <div className="list">
        {data.map((slice, index) => (
          <div className="row" key={slice.label}>
            <div>
              <strong style={{ color: colors[index % colors.length] }}>{slice.label}</strong>
              <small>{percent(slice.percentage)} of assets</small>
            </div>
            <strong>{money(slice.value)}</strong>
          </div>
        ))}
      </div>
    </div>
  );
}

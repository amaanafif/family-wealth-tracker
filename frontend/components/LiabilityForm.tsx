"use client";

import { FormEvent, useState } from "react";
import { Plus, Trash2 } from "lucide-react";
import { createLiability, deleteLiability } from "@/lib/api";
import { money } from "@/lib/format";
import type { Liability } from "@/types/wealth";

type Props = {
  liabilities: Liability[];
  onChanged: () => void;
};

const initial: Liability = {
  name: "",
  outstandingAmount: 0,
  interestRate: 0,
};

export function LiabilityForm({ liabilities, onChanged }: Props) {
  const [liability, setLiability] = useState<Liability>(initial);
  const [saving, setSaving] = useState(false);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    await createLiability({
      ...liability,
      outstandingAmount: Number(liability.outstandingAmount),
      interestRate: Number(liability.interestRate),
    });
    setLiability(initial);
    setSaving(false);
    onChanged();
  }

  async function remove(id?: number) {
    if (!id) return;
    await deleteLiability(id);
    onChanged();
  }

  return (
    <div className="panel">
      <div className="panel-title">
        <h2>Liabilities</h2>
      </div>
      <form onSubmit={submit}>
        <div className="form-grid">
          <div className="field">
            <label>Name</label>
            <input required value={liability.name} onChange={(event) => setLiability({ ...liability, name: event.target.value })} />
          </div>
          <div className="field">
            <label>Outstanding</label>
            <input min="0" step="0.01" type="number" value={liability.outstandingAmount} onChange={(event) => setLiability({ ...liability, outstandingAmount: Number(event.target.value) })} />
          </div>
          <div className="field full">
            <label>Interest rate</label>
            <input min="0" step="0.01" type="number" value={liability.interestRate ?? 0} onChange={(event) => setLiability({ ...liability, interestRate: Number(event.target.value) })} />
          </div>
        </div>
        <div className="actions">
          <button disabled={saving} type="submit">
            <Plus size={18} />
            Add liability
          </button>
        </div>
      </form>
      <div className="list">
        {liabilities.map((item) => (
          <div className="row" key={item.id}>
            <div>
              <strong>{item.name}</strong>
              <small>
                {money(item.outstandingAmount)} · {item.interestRate ?? 0}% APR
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

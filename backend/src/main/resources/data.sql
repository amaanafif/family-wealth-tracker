-- Create tables for H2 local development
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS prices (
    id UUID PRIMARY KEY,
    symbol TEXT,
    type TEXT,
    value NUMERIC,
    last_updated TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS portfolio (
    symbol TEXT PRIMARY KEY,
    name TEXT,
    isin TEXT,
    type TEXT,
    sector TEXT,
    scheme_type TEXT,
    quantity NUMERIC,
    avg_price NUMERIC,
    last_price NUMERIC,
    invested_value NUMERIC,
    current_value NUMERIC,
    pnl NUMERIC,
    pnl_pct NUMERIC
);

CREATE TABLE IF NOT EXISTS holdings (
    id UUID PRIMARY KEY,
    owner_id UUID,
    quantity NUMERIC,
    quantity_long_term NUMERIC,
    quantity_pledged_margin NUMERIC,
    quantity_pledged_loan NUMERIC,
    avg_price NUMERIC,
    last_price NUMERIC,
    invested_value NUMERIC,
    current_value NUMERIC,
    pnl NUMERIC,
    pnl_pct NUMERIC,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    name TEXT,
    symbol TEXT,
    isin TEXT,
    type TEXT,
    sector TEXT,
    scheme_type TEXT
);

CREATE TABLE IF NOT EXISTS portfolio_summary (
    type TEXT PRIMARY KEY,
    total_invested NUMERIC,
    total_current NUMERIC,
    total_pnl NUMERIC
);

-- Insert sample data
INSERT INTO users (id, name, created_at)
VALUES
('11111111-1111-1111-1111-111111111111', 'User', NOW());

INSERT INTO portfolio (symbol, name, isin, type, sector, scheme_type, quantity, avg_price, last_price, invested_value, current_value, pnl, pnl_pct)
VALUES
  ('NIFTY', 'Nifty 50 ETF', 'INF200K01QE2', 'STOCK', 'Index', NULL, 1, 30000, 2800, 30000, 2800, -27200, -90.67),
  ('VTSAX', 'US Total Market Fund', '922908728', 'MF', 'Equity', 'Large Cap', 1, 84320, 120, 84320, 120, -84200, -99.86);

INSERT INTO holdings (id, owner_id, quantity, quantity_long_term, quantity_pledged_margin, quantity_pledged_loan, avg_price, last_price, invested_value, current_value, pnl, pnl_pct, created_at, updated_at, name, symbol, isin, type, sector, scheme_type)
VALUES
  (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 1, 1, 0, 0, 30000, 2800, 30000, 2800, -27200, -90.67, NOW(), NOW(), 'Nifty 50 ETF', 'NIFTY', 'INF200K01QE2', 'STOCK', 'Index', NULL),
  (RANDOM_UUID(), '11111111-1111-1111-1111-111111111111', 1, 1, 0, 0, 84320, 120, 84320, 120, -84200, -99.86, NOW(), NOW(), 'US Total Market Fund', 'VTSAX', '922908728', 'MF', 'Equity', 'Large Cap');

INSERT INTO portfolio_summary (type, total_invested, total_current, total_pnl)
VALUES
  ('ALL', 114320, 2920, -111400);

INSERT INTO prices (id, symbol, type, value, last_updated)
VALUES
  (RANDOM_UUID(), 'NIFTY', 'STOCK', 2800, NOW()),
  (RANDOM_UUID(), 'VTSAX', 'MF', 120, NOW())
ON CONFLICT (symbol, type) DO UPDATE SET
  value = EXCLUDED.value,
  last_updated = NOW();

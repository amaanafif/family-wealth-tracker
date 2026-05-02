insert into asset (name, symbol, type, bucket, quantity, manual_price, created_at, updated_at)
values
  ('Nifty 50 ETF', 'NIFTYBEES.NS', 'STOCK', 'LARGE_CAP', 120, 250.00, current_timestamp, current_timestamp),
  ('US Total Market Fund', 'foreign-mf', 'MUTUAL_FUND', 'FOREIGN', 800, 105.40, current_timestamp, current_timestamp),
  ('Bitcoin', 'bitcoin', 'CRYPTO', 'CRYPTO', 0.08, 5200000.00, current_timestamp, current_timestamp),
  ('Primary Residence', null, 'REAL_ESTATE', 'REAL_ESTATE', 1, 9500000.00, current_timestamp, current_timestamp),
  ('Emergency Fund', null, 'CASH', 'DEBT_CASH', 1, 850000.00, current_timestamp, current_timestamp);

insert into liability (name, outstanding_amount, interest_rate, created_at, updated_at)
values
  ('Home Loan', 3200000.00, 8.50, current_timestamp, current_timestamp);

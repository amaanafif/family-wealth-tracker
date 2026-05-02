import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Family Wealth Tracker",
  description: "Track family assets, allocation, and smart price syncs.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}

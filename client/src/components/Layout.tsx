import { ReactNode } from "react";
import { Link } from "react-router-dom";

export default function Layout({ children }: { children: ReactNode }) {
  return (
    <div className="min-h-screen flex flex-col">
      {/* Navbar */}
      <header className="bg-gray-800 text-white p-4 flex justify-between">
        <h1 className="text-xl font-bold">Collab Task Manager</h1>
        <nav>
          <Link to="/dashboard" className="mr-4 hover:underline">Dashboard</Link>
          <Link to="/login" className="hover:underline">Login</Link>
        </nav>
      </header>

      {/* Page Content */}
      <main className="flex-1 p-4 bg-gray-50">{children}</main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white p-2 text-center text-sm">
        &copy; 2025 Collaborative Task Manager
      </footer>
    </div>
  );
}

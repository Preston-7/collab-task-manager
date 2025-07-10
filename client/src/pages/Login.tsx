import React from "react";
import { Link } from "react-router-dom";

export default function Login() {
  return (
    <div className="flex flex-col items-center justify-center w-full max-w-sm p-6 bg-white rounded-lg shadow-lg">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">Login</h1>
      <form className="flex flex-col w-full space-y-4">
        <input
          type="text"
          placeholder="Username"
          className="px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <input
          type="password"
          placeholder="Password"
          className="px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
        />
        <button
          type="submit"
          className="w-full px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700"
        >
          Login
        </button>
      </form>
      <p className="mt-4 text-gray-600">
        Don’t have an account?{" "}
        <Link
          to="/signup"
          className="text-indigo-600 hover:underline"
        >
          Sign up
        </Link>
      </p>
    </div>
  );
}

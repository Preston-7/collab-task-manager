import api from "./axios";

export const login = async (username: string, password: string) => {
  try {
    const response = await api.get("/tasks", {
      auth: {
        username,
        password,
      },
    });
    return response.data; // Return user tasks or success indicator
  } catch (error: any) {
    console.error("Login failed:", error.response?.data || error.message);
    throw new Error(
      error.response?.data?.message || "Invalid username or password"
    );
  }
};

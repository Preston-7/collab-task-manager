import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080", // Backend API
  withCredentials: true, // Send cookies (optional, if using sessions)
});

export default instance;

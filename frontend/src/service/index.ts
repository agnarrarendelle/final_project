import axios from "axios";
import { useStore } from "../state";

const store = useStore()

const axiosInstance = axios.create({
  baseURL: "http://localhost:7000",
  withCredentials: true,
});

axiosInstance.interceptors.request.use(
  (config) => {
    const token = store.getters.jwtAuthToken
    console.log("token is " + token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },

  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;

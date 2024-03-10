import axios from "..";
import { ApiResponse, UserResponse } from "../response_types";

const RootApiPath = "/user";

export const login = async (
  name: string,
  password: string
): Promise<ApiResponse<UserResponse>> => {
  const res = await axios.post(`${RootApiPath}/login`, { name, password });
  return res.data;
};

export const register = async (
  name: string,
  email: string,
  password: string
): Promise<ApiResponse<void>> => {
  const res = await axios.post(`${RootApiPath}/register`, { name, email, password });
  return res.data
};

export const searchUsers = async (
  groupId: number,
  name: string
): Promise<ApiResponse<UserResponse[]>> => {
  const res = await axios.get(`${RootApiPath}?groupId=${groupId}&name=${name}`);
  return res.data;
};

export const checkUserLogin = async () => {
  try {
    await axios.put(RootApiPath);
    return true;
  } catch (e) {
    return false;
  }
};

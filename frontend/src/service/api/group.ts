import axios from "..";
import { ChatMessage, TaskBody } from "../request_types";
import {
  ApiResponse,
  CategoryResponse,
  GroupResponse,
  TaskResponse,
  UserResponse,
} from "../response_types";

const apiRoot = "/group";

export const addGroup = async (
  name: string
): Promise<ApiResponse<GroupResponse>> => {
  const res = await axios.post(apiRoot, {
    name,
  });

  return res.data;
};

export const getGroups = async (): Promise<ApiResponse<GroupResponse[]>> => {
  const res = await axios.get(apiRoot);
  return res.data;
};

export const getGroupUsers = async (
  groupId: number
): Promise<ApiResponse<UserResponse[]>> => {
  const res = await axios.get(`${apiRoot}/${groupId}/users`);
  return res.data;
};

export const addGroupUser = async (
  groupId: number,
  userId: number
): Promise<ApiResponse<UserResponse>> => {
  const res = await axios.post(`${apiRoot}/${groupId}/users`, { id: userId });
  return res.data;
};

export const getGroupCategories = async (
  groupId: number
): Promise<ApiResponse<CategoryResponse[]>> => {
  const res = await axios.get(`${apiRoot}/${groupId}/categories`);
  return res.data;
};

export const addGroupCategory = async (
  groupId: number,
  name: string
): Promise<ApiResponse<CategoryResponse>> => {
  const res = await axios.post(`${apiRoot}/${groupId}/category`, { name });
  return res.data;
};

export const getGroupTask = async (
  groupId: number,
  taskId: number
): Promise<ApiResponse<TaskResponse>> => {
  const res = await axios.get(`${apiRoot}/${groupId}/task/${taskId}`);
  return res.data;
};

export const deleteGroupTask = async (
  groupId: number,
  taskId: number
): Promise<ApiResponse<null>> => {
  const res = await axios.delete(`${apiRoot}/${groupId}/task/${taskId}`);
  return res.data;
};

export const updateTaskStatus = async (
  groupId: number,
  taskId: number
): Promise<ApiResponse<TaskResponse>> => {
  const res = await axios.put(`${apiRoot}/${groupId}/task/${taskId}/status`);
  return res.data;
};

export const getGroupTasks = async (
  groupId: number
): Promise<ApiResponse<TaskResponse[]>> => {
  const res = await axios.get(`${apiRoot}/${groupId}/tasks`);
  return res.data;
};

export const addGroupTask = async (
  groupId: number,
  categoryId: number,
  body: TaskBody
): Promise<ApiResponse<TaskResponse>> => {
  const res = await axios.post(
    `${apiRoot}/${groupId}/category/${categoryId}/task`,
    body
  );
  return res.data;
};

export const modifyGroupTask = async (
  groupId: number,
  taskId: number,
  body: TaskBody
): Promise<ApiResponse<TaskResponse>> => {
  const res = await axios.put(`${apiRoot}/${groupId}/task/${taskId}`, body);
  return res.data;
};

export const getChat = async (
  groupId: number
): Promise<ApiResponse<ChatMessage[]>> => {
  const res = await axios.get(`${apiRoot}/${groupId}/chat`);
  return res.data;
};

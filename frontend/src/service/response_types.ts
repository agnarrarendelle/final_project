import { TaskStatus } from "./request_types";

export interface ApiResponse<T> {
  msg: string;
  data: T;
}

export interface GroupResponse {
  id: number;
  name: string;
}

export interface CategoryResponse {
  id: number;
  name: string;
}

export interface TaskResponse {
  readonly id: number;
  name: string;
  status: TaskStatus;
  priorityLevel: string;
  expiredAt: Date;
  categoryName: string;
}

export interface TaskWsResponse {
  task: TaskResponse;
  type: "Modified" | "Created";
}

export interface UserResponse {
  id: number;
  name: string;
  token: string;
}

export enum FilterTaskOptionByTaskStatus {
  AllTask = "All Tasks",
  CompletedTasks = "Completed Tasks",
  IncompletedTasks = "Incompleted Tasks",
  Expired = "Expired"
}

export enum SortTaskOption {
  Priority = "Priority",
  Name = "Name",
  CreatedTime = "Created Time"
}
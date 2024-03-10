export interface CategoryBody {
  groupId: number;
  name: string;
}

export interface TaskBody {
  name: string;
  status: string;
  priorityLevel: string;
  expiredAt: Date;
}

export enum TaskPriorityLevel {
  High = "High",
  Medium = "Medium",
  Low = "Low",
}

export enum TaskStatus {
  InProgress = "InProgress",
  Finished = "Finished",
  Expired = "Expired",
}

export interface ChatMessage {
  message: string;
  userId: number;
  userName:string
}

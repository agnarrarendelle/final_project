import { Client } from "@stomp/stompjs";
import {
  CategoryResponse,
  FilterTaskOptionByTaskStatus,
  SortTaskOption,
  TaskResponse,
  UserResponse,
} from "../service/response_types";
import { ChatMessage, TaskStatus } from "../service/request_types";

export type State = {
  user: UserResponse;
  groupDetails: Map<
    number,
    {
      id: number;
      name: string;
      categories: CategoryResponse[];
      tasks: TaskResponse[];
      filteredTasks:TaskResponse[]
      users: UserResponse[];
      chatMessages: ChatMessage[]
      taskStatusMap:Map<TaskStatus, number>
      sortAndFilteredTask:{
        searchName:string,
        taskStatus:FilterTaskOptionByTaskStatus,
        sortOption:SortTaskOption,
        categoryName:string
      }
    }
  >;
  websocket: Client | null;
};

export const state: State = {
  user: { name: "", token: "", id: -1 },
  groupDetails: new Map(),
  websocket: null,
};

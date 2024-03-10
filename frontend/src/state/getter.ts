import { GetterTree } from "vuex";
import { State } from "./state";
import {
  CategoryResponse,
  TaskResponse,
  UserResponse,
  GroupResponse,
  FilterTaskOptionByTaskStatus,
  SortTaskOption,
} from "../service/response_types";
import { Client } from "@stomp/stompjs";
import { ChatMessage, TaskStatus } from "../service/request_types";

export type Getters = {
  jwtAuthToken(state: State): string | null;
  userId(state: State): number;
  userName(state: State): string;
  groups(state: State): GroupResponse[];
  groupName(state: State): (groupId: number) => string | undefined;
  groupCategory(
    state: State
  ): (groupId: number, categoryId: number) => CategoryResponse;
  groupCategories(state: State): (groupId: number) => CategoryResponse[];
  groupTask(state: State): (groupId: number, taskId: number) => TaskResponse;
  groupTasks(state: State): (groupId: number) => TaskResponse[];
  groupTasksNumber(
    state: State
  ): (groupId: number, status: TaskStatus) => number;
  filteredGroupTasks(state: State): (groupId: number) => TaskResponse[];
  filterAndSortOptions(state: State): (groupId: number) => {
    searchName: string;
    taskStatus: FilterTaskOptionByTaskStatus;
    sortOption: SortTaskOption;
    categoryName: string;
  };
  groupUsers(state: State): (groupId: number) => UserResponse[];
  groupChatMessages(state: State): (groupId: number) => ChatMessage[];
  isGroupExist(state: State): (groupId: number) => boolean;
  wsClient(state: State): Client | null;
};

export const getters: GetterTree<State, State> & Getters = {
  jwtAuthToken: (state) => state.user.token,
  userId: (state) => state.user.id,
  userName: (state) => state.user.name,
  groups: (state) =>
    Array.from(state.groupDetails, ([groupId, groupDetail]) => ({
      id: groupId,
      name: groupDetail.name,
    })),
  groupName: (state) => (groupId) => state.groupDetails.get(groupId)!.name,
  groupCategory: (state) => (groupId, categoryId) =>
    state.groupDetails
      .get(groupId)!
      .categories.find((c) => c.id === categoryId)!,
  groupCategories: (state) => (groupId) =>
    state.groupDetails.get(groupId)!.categories,
  groupTask: (state) => (groupId, taskId) =>
    state.groupDetails.get(groupId)!.tasks.find((t) => t.id === taskId)!,
  groupTasks: (state) => (groupId) =>
    state.groupDetails.get(groupId)?.tasks ?? [],
  groupTasksNumber: (state) => (groupId, status) =>
    state.groupDetails.get(groupId)?.taskStatusMap.get(status)!,
  filteredGroupTasks: (state) => (groupId) =>
    state.groupDetails.get(groupId)?.filteredTasks ?? [],
  filterAndSortOptions: (state) => (groupId) =>
    state.groupDetails.get(groupId)?.sortAndFilteredTask!,
  groupUsers: (state) => (groupId) =>
    state.groupDetails.get(groupId)?.users ?? [],
  groupChatMessages: (state) => (groupId) =>
    state.groupDetails.get(groupId)!.chatMessages,
  isGroupExist: (state) => (groupId) => state.groupDetails.has(groupId),
  wsClient: (state) => state.websocket,
};

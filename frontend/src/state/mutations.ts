import { State } from "./state";
import { MutationTypes } from "./mutation-types";
import { MutationTree } from "vuex";
import {
  CategoryResponse,
  FilterTaskOptionByTaskStatus,
  SortTaskOption,
  TaskResponse,
  TaskWsResponse,
  UserResponse,
} from "../service/response_types";
import { Client } from "@stomp/stompjs";
import { ChatMessage, TaskStatus } from "../service/request_types";

export type Mutations<S = State> = {
  [MutationTypes.SET_USER](state: S, user: UserResponse): void;
  [MutationTypes.INIT_GROUP_DETAILS](
    state: S,
    { groupId, name }: { groupId: number; name: string }
  ): void;
  [MutationTypes.ADD_GROUP_CATEGORY](
    states: S,
    { groupId, category }: { groupId: number; category: CategoryResponse }
  ): void;
  [MutationTypes.ADD_GROUP_TASK](
    states: S,
    { groupId, task }: { groupId: number; task: TaskResponse }
  ): void;
  [MutationTypes.DELETE_GROUP_TASK](
    states: S,
    {
      groupId,
      taskId,
      taskStatus,
    }: { groupId: number; taskId: number; taskStatus: TaskStatus }
  ): void;
  [MutationTypes.SET_FILTERED_GROUP_TASK](
    states: S,
    {
      groupId,
      filterBy,
    }: {
      groupId: number;
      filterBy: (task: TaskResponse) => boolean;
    }
  ): void;
  [MutationTypes.SET_SORT_AND_FILTER_OPTION](
    states: S,
    {
      groupId,
      options,
    }: {
      groupId: number;
      options: {
        searchName?: string;
        taskStatus?: FilterTaskOptionByTaskStatus;
        sortOption?: SortTaskOption;
        categoryName?: string;
      };
    }
  ): void;
  [MutationTypes.SORT_FILTERED_GROUP_TASK](
    states: S,
    {
      groupId,
      sortBy,
    }: {
      groupId: number;
      sortBy: ((a: TaskResponse, b: TaskResponse) => number) | undefined;
    }
  ): void;
  [MutationTypes.ADD_GROUP_USER](
    states: S,
    { groupId, user }: { groupId: number; user: UserResponse }
  ): void;
  [MutationTypes.INIT_WS_CLIENT](state: S): void;
  [MutationTypes.ADD_WS_GROUP_CHAT_SUBSCRIPTION](
    state: S,
    groupId: number
  ): void;
  [MutationTypes.ADD_WS_GROUP_CHAT_MESSAGES](
    state: S,
    { groupId, messages }: { groupId: number; messages: ChatMessage[] }
  ): void;
  [MutationTypes.ADD_WS_GROUP_TASK_SUBSCRIPTION](
    state: S,
    groupId: number
  ): void;
};

export const mutations: MutationTree<State> & Mutations = {
  [MutationTypes.SET_USER](state, user: UserResponse) {
    state.user = user;
  },
  [MutationTypes.INIT_GROUP_DETAILS](
    state,
    { groupId, name }: { groupId: number; name: string }
  ): void {
    state.groupDetails.set(groupId, {
      name: name,
      id: groupId,
      categories: [],
      tasks: [],
      filteredTasks: [],
      users: [],
      chatMessages: [],
      taskStatusMap: new Map(Object.values(TaskStatus).map((key) => [key, 0])),
      sortAndFilteredTask: {
        searchName: "",
        taskStatus: FilterTaskOptionByTaskStatus.AllTask,
        sortOption: SortTaskOption.Name,
        categoryName: "",
      },
    });
  },
  [MutationTypes.ADD_GROUP_CATEGORY](
    state,
    { groupId, category }: { groupId: number; category: CategoryResponse }
  ): void {
    state.groupDetails.get(groupId)?.categories.push(category);
  },
  [MutationTypes.ADD_GROUP_TASK](
    state,
    { groupId, task }: { groupId: number; task: TaskResponse }
  ): void {
    const groupDeatils = state.groupDetails.get(groupId);

    groupDeatils?.tasks.push(task);

    const taskStatus = task.status;

    groupDeatils?.taskStatusMap.set(
      taskStatus,
      groupDeatils?.taskStatusMap.get(taskStatus)! + 1
    );
  },
  [MutationTypes.DELETE_GROUP_TASK](
    state,
    {
      groupId,
      taskId,
      taskStatus,
    }: { groupId: number; taskId: number; taskStatus: TaskStatus }
  ): void {
    const groupTasks = state.groupDetails.get(groupId)?.tasks;
    const filteredArr = groupTasks?.filter((t) => t.id !== taskId)!;

    groupTasks!.length = 0;
    groupTasks!.push(...filteredArr);

    const taskStatusMap = state.groupDetails.get(groupId)?.taskStatusMap;

    taskStatusMap?.set(taskStatus, taskStatusMap.get(taskStatus)! - 1);
  },
  [MutationTypes.SET_FILTERED_GROUP_TASK](
    states,
    {
      groupId,
      filterBy,
    }: {
      groupId: number;
      filterBy: (task: TaskResponse) => boolean;
    }
  ): void {
    const groupTasks = states.groupDetails.get(groupId)?.tasks!;

    const filteredGroupTasks = states.groupDetails.get(groupId)?.filteredTasks!;

    filteredGroupTasks.length = 0;

    filteredGroupTasks.push(...groupTasks.filter(filterBy));
  },
  [MutationTypes.SORT_FILTERED_GROUP_TASK](
    states,
    {
      groupId,
      sortBy,
    }: {
      groupId: number;
      sortBy: ((a: TaskResponse, b: TaskResponse) => number) | undefined;
    }
  ): void {
    const filteredGroupTasks = states.groupDetails.get(groupId)?.filteredTasks!;
    filteredGroupTasks.sort(sortBy);
  },
  [MutationTypes.SET_SORT_AND_FILTER_OPTION](
    states,
    {
      groupId,
      options,
    }: {
      groupId: number;
      options: {
        searchName?: string;
        taskStatus?: FilterTaskOptionByTaskStatus;
        sortOption?: SortTaskOption;
        categoryName?: string;
      };
    }
  ): void {
    const sortAndFilteredTask =
      states.groupDetails.get(groupId)?.sortAndFilteredTask!;

    if (options.searchName && options.searchName.length > 0)
      sortAndFilteredTask.searchName = options.searchName;

    if (options.taskStatus) {
      sortAndFilteredTask.taskStatus = options.taskStatus;
    }

    if (options.sortOption) sortAndFilteredTask.sortOption = options.sortOption;

    if (options.categoryName || options.categoryName === "")
      sortAndFilteredTask.categoryName = options.categoryName;
  },
  [MutationTypes.ADD_GROUP_USER](
    state,
    { groupId, user }: { groupId: number; user: UserResponse }
  ): void {
    state.groupDetails.get(groupId)?.users.push(user);
  },
  [MutationTypes.ADD_WS_GROUP_CHAT_MESSAGES](
    state,
    { groupId, messages }: { groupId: number; messages: ChatMessage[] }
  ): void {
    state.groupDetails.get(groupId)?.chatMessages.push(...messages);
  },
  [MutationTypes.INIT_WS_CLIENT](state): void {
    const client = new Client({
      brokerURL: "ws://localhost:7000/websocket",
      onConnect: () => {
        console.log("ws connected");
      },
      onWebSocketError: (e) => {
        console.error(e);
      },
      onStompError: async (e) => {
        console.log(e);
        alert("Failed to get order status. Please re-login again");
        await client.deactivate();
      },
    });

    window.onbeforeunload = async () => {
      await client.deactivate();
    };

    state.websocket = client;

    state.websocket.activate();
  },
  [MutationTypes.ADD_WS_GROUP_CHAT_SUBSCRIPTION](state, groupId: number): void {
    const client = state.websocket;
    if (client === null) {
      console.error("Ws not connected");
      return;
    }
    client.subscribe(`/topic/group-messages/${groupId}`, (message) => {
      console.log(`Get message from group ${groupId}`);
      const newMessage: ChatMessage = JSON.parse(message.body);
      console.log(newMessage);
      state.groupDetails.get(groupId)?.chatMessages.push(newMessage);
    });
  },
  [MutationTypes.ADD_WS_GROUP_TASK_SUBSCRIPTION](state, groupId: number): void {
    const client = state.websocket;
    if (client === null) {
      console.error("Ws not connected");
      return;
    }
    client.subscribe(
      `/topic/group-messages/${groupId}/task`,
      async (message) => {
        const modifiedTask: TaskWsResponse = JSON.parse(message.body);
        const tasks = state.groupDetails.get(groupId)!.tasks;

        switch (modifiedTask.type) {
          case "Modified": {
            const oldTaskIndex = tasks.findIndex(
              (t) => t.id === modifiedTask.task.id
            );

            if (tasks[oldTaskIndex].status !== modifiedTask.task.status) {
              const taskStatus =
                state.groupDetails.get(groupId)?.taskStatusMap!;

              taskStatus.set(
                tasks[oldTaskIndex].status,
                taskStatus.get(tasks[oldTaskIndex].status)! - 1
              );

              const flipedStatus =
                tasks[oldTaskIndex].status === TaskStatus.InProgress
                  ? TaskStatus.Finished
                  : TaskStatus.InProgress;

              taskStatus.set(flipedStatus, taskStatus.get(flipedStatus)! + 1);
            }
            tasks[oldTaskIndex] = modifiedTask.task;
            break;
          }
          case "Created": {
            console.log(modifiedTask.task);
            tasks.push(modifiedTask.task);
            break;
          }
        }
      }
    );
  },
};

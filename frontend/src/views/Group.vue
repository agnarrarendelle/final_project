<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { getChat, getGroupCategories, getGroupTasks, getGroupUsers } from '../service/api/group';
import ChatRoom from '../components/ChatRoom.vue';
import Menu from "../components/Menu.vue"
import Footer from "../components/Footer.vue"
import TaskSection from "../components/TaskSection.vue"
import AccountData from "../components/AccountData.vue"

import { useStore } from '../state';
import { MutationTypes } from '../state/mutation-types';
import { FilterTaskOptionByTaskStatus, SortTaskOption, TaskResponse } from '../service/response_types';
import { TaskStatus } from '../service/request_types';

const store = useStore()

const route = useRoute()
const groupId = parseInt(route.params.id as unknown as string)

const isChatRoomOpen = ref(false)

const filteredAndSortedTasks = computed(() => {
    const { searchName, sortOption, taskStatus, categoryName } = store.getters.filterAndSortOptions(groupId)

    const filteredTaskByStatus = () => {
        switch (taskStatus) {
            case FilterTaskOptionByTaskStatus.AllTask:
                return (_: TaskResponse) => true
            case FilterTaskOptionByTaskStatus.CompletedTasks:
                return (task: TaskResponse) => task.status === "Finished"
            case FilterTaskOptionByTaskStatus.IncompletedTasks:
                return (task: TaskResponse) => task.status === "InProgress"
            case FilterTaskOptionByTaskStatus.Expired:
                return (task: TaskResponse) => task.status === "Expired"
            default:
                return (_: TaskResponse) => true
        }
    }

    const sortedFunction: ((a: TaskResponse, b: TaskResponse) => number) | undefined = (() => {
        switch (sortOption) {
            case SortTaskOption.Priority:
                return (a, b) => Object.values(TaskStatus).indexOf(a.status) - Object.values(TaskStatus).indexOf(b.status)
            case SortTaskOption.Name:
                return (a, b) => a.name.localeCompare(b.name)
            case SortTaskOption.CreatedTime:
                return (a, b) => a.expiredAt.toString().localeCompare(b.expiredAt.toString())
            default:
                return (a, b) => a.name.localeCompare(b.name)
        }
    })()

    const result = store.state.groupDetails.
        get(groupId)?.tasks
        .filter((t) => {
            const cond1 = filteredTaskByStatus()(t)
            const cond2 = searchName.trim().length > 0 ? t.name.includes(searchName) : true
            const cond3 = categoryName.trim().length > 0 ? t.categoryName.trim() === categoryName : true
            return cond1 && cond2 && cond3
        })
        .sort(sortedFunction)!

    return result
})

onMounted(async () => {
    const categoriesResponse = await getGroupCategories(groupId)
    const tasksResponse = await getGroupTasks(groupId)
    const usersResponse = await getGroupUsers(groupId)
    const groupChatMessages = await getChat(groupId);

    categoriesResponse.data.forEach(c => store.commit(MutationTypes.ADD_GROUP_CATEGORY, { groupId, category: c }))
    tasksResponse.data.forEach(t => store.commit(MutationTypes.ADD_GROUP_TASK, { groupId, task: t }))
    usersResponse.data.forEach(u => store.commit(MutationTypes.ADD_GROUP_USER, { groupId, user: u }))

    store.commit(MutationTypes.SET_FILTERED_GROUP_TASK, { groupId: groupId, filterBy: (_) => true })

    store.commit(MutationTypes.ADD_WS_GROUP_CHAT_SUBSCRIPTION, groupId)
    store.commit(MutationTypes.ADD_WS_GROUP_CHAT_MESSAGES, { groupId: groupId, messages: groupChatMessages.data })
    store.commit(MutationTypes.ADD_WS_GROUP_TASK_SUBSCRIPTION, groupId)
})




</script>
<template>
    <div
        class="bg-slate-200 min-h-screen text-slate-600 dark:bg-slate-900 dark:text-slate-400 xl:text-base sm:text-sm text-xs">
        <Menu :group-id="groupId"></Menu>
        <TaskSection :group-id="groupId" :task-list="filteredAndSortedTasks"></TaskSection>
        <ChatRoom v-if="isChatRoomOpen" :close-modal="() => isChatRoomOpen = false" :group-id="groupId"></ChatRoom>
        <Footer></Footer>
        <AccountData :group-id="groupId"></AccountData>
        <button @click="() => isChatRoomOpen = true">Open Chat Room</button>
    </div>
</template>

<style scoped></style>

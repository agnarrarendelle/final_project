<script setup lang="ts">
import { ref } from 'vue';
import { useStore } from "../state"
import { MutationTypes } from '../state/mutation-types';
import TaskDetailModal from './TaskDetailModal.vue';
import { deleteGroupTask, updateTaskStatus } from '../service/api/group';
import { TaskResponse, TaskWsResponse } from '../service/response_types';
import { TaskStatus } from '../service/request_types';

const store = useStore()

const props = defineProps<{
    groupId: number
    taskList: TaskResponse[]
}>()

const isModifyTaskModalOpen = ref(false)
const selectedTask = ref<TaskResponse | null>(null)

const flipTaskStatus = async (taskId: number) => {
    const res = await updateTaskStatus(props.groupId, taskId)

    const websocket = store.getters.wsClient!;

    const wsBody: TaskWsResponse = {
        type: "Modified",
        task: res.data
    }
    websocket.publish({
        destination: `/app/group/${props.groupId}/task/${res.data.id}`,
        body: JSON.stringify(wsBody)
    })
}

const isDateAboutToDue = (date: Date): Boolean => {
    const futureDate = new Date(date);

    const currentDate = new Date();

    const differenceInMs = futureDate.getTime() - currentDate.getTime();

    const differenceInDays = differenceInMs / (1000 * 3600 * 24);

    // Check if the current date is within the desired range
    const isWithinDays = differenceInDays <= 3;

    return isWithinDays
}

const isDateExpired = (date: Date): Boolean => {
    const myDate = (new Date(date)).getTime();

    const currentDate = (new Date()).getTime();
    console.log("expired: " + (myDate < currentDate))
    return myDate < currentDate
}

const canTaskBeFlipped = (task: TaskResponse) => {
    return !isDateExpired(task.expiredAt) && (task.status === TaskStatus.InProgress || task.status === TaskStatus.Finished)
}

const deleteTask = (taskId: number, status: TaskStatus) => {
    if (!confirm(`Do you want to delete task ${taskId}`))
        return

    deleteGroupTask(props.groupId, taskId)

    store.commit(MutationTypes.DELETE_GROUP_TASK, {
        groupId: props.groupId,
        taskId: taskId,
        taskStatus: status
    })

    alert(`Task ${taskId} deleted`)
}

</script>
<template>
    <h1 class="text-rose-800">**Expired tasks would be deleted every 3 days**</h1>
    <ul
        class="tasksList mt-4 grid gap-2 sm:gap-4 xl:gap-6 2xl:grid-cols-4 xl:grid-cols-3 lg:grid-cols-4 md:grid-cols-3 grid-cols-2 items-end">
        <li v-for="task in props.taskList">
            <article
                class="bg-slate-100 rounded-lg p-3 sm:p-4 flex text-left transition hover:shadow-lg hover:shadow-slate-300 dark:bg-slate-800 dark:hover:shadow-transparent flex-col h-52 sm:h-64">
                <div class="flex flex-col flex-1 ">
                    <div class="flex items-center justify-between mb-2">
                        <span :title="task.name" class="block font-medium dark:text-slate-200">
                            {{ task.name.length > 10 ? `${task.name.slice(0, 10)}...` : task.name }}
                        </span>
                        <span class="block font-medium dark:text-slate-200 text-sky-500">
                            {{ task.categoryName }}
                        </span>
                    </div>
                    <p v-if="!isDateExpired(task.expiredAt) && isDateAboutToDue(task.expiredAt) && task.status === TaskStatus.InProgress"
                        class="mb-2 line-clamp-3 text-rose-600">About to Due!!!
                    </p>
                    <p v-else-if="isDateExpired(task.expiredAt) && (task.status === TaskStatus.InProgress || task.status === TaskStatus.Expired)"
                        class="mb-2 line-clamp-3 text-rose-600">Expired
                    </p>
                    <time class="mt-auto flex w-full">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                            stroke="currentColor" class="mr-2 w-4 sm:w-5">
                            <path stroke-linecap="round" stroke-linejoin="round"
                                d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5m-9-6h.008v.008H12v-.008zM12 15h.008v.008H12V15zm0 2.25h.008v.008H12v-.008zM9.75 15h.008v.008H9.75V15zm0 2.25h.008v.008H9.75v-.008zM7.5 15h.008v.008H7.5V15zm0 2.25h.008v.008H7.5v-.008zm6.75-4.5h.008v.008h-.008v-.008zm0 2.25h.008v.008h-.008V15zm0 2.25h.008v.008h-.008v-.008zm2.25-4.5h.008v.008H16.5v-.008zm0 2.25h.008v.008H16.5V15z">
                            </path>
                        </svg>
                        {{ task.expiredAt }}
                    </time>
                </div>
                <div class="flex border-dashed border-slate-200 dark:border-slate-700/[.3] border-t-2 w-full pt-4 mt-4">
                    <button v-if="canTaskBeFlipped(task)" @click="flipTaskStatus(task.id)" title="mark as uncompleted"
                        :class="`${task.status === 'InProgress' ? 'bg-emerald-200 text-emerald-800 ' : 'bg-amber-200 text-amber-800 '} mr-4 order-0 rounded-full font-medium`">
                        <span class="block py-1 px-3 absolute invisible sm:static sm:visible">{{ task.status }}</span>
                        <span class=" sm:hidden w-6 h-6 grid place-items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                                stroke="currentColor" class="w-3 h-3">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5">
                                </path>
                            </svg>
                        </span>
                    </button>
                    <button v-else class="bg-green-200 text-amber-800 mr-4 order-0 rounded-full font-medium">
                        <span class="block py-1 px-3 absolute invisible sm:static sm:visible">{{ task.status }}</span>
                        <span class=" sm:hidden w-6 h-6 grid place-items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                                stroke="currentColor" class="w-3 h-3">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5">
                                </path>
                            </svg>
                        </span>
                    </button>
                    <button class="transition hover:text-slate-700 dark:hover:text-slate-200 ml-auto">
                        {{ task.priorityLevel }}
                    </button>
                    <button @click="deleteTask(task.id, task.status)" title="delete task"
                        class="ml-2 transition hover:text-slate-700 dark:hover:text-slate-200">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor"
                            class="w-5 h-5 sm:w-6 sm:h-6">
                            <path fill-rule="evenodd"
                                d="M16.5 4.478v.227a48.816 48.816 0 013.878.512.75.75 0 11-.256 1.478l-.209-.035-1.005 13.07a3 3 0 01-2.991 2.77H8.084a3 3 0 01-2.991-2.77L4.087 6.66l-.209.035a.75.75 0 01-.256-1.478A48.567 48.567 0 017.5 4.705v-.227c0-1.564 1.213-2.9 2.816-2.951a52.662 52.662 0 013.369 0c1.603.051 2.815 1.387 2.815 2.951zm-6.136-1.452a51.196 51.196 0 013.273 0C14.39 3.05 15 3.684 15 4.478v.113a49.488 49.488 0 00-6 0v-.113c0-.794.609-1.428 1.364-1.452zm-.355 5.945a.75.75 0 10-1.5.058l.347 9a.75.75 0 101.499-.058l-.346-9zm5.48.058a.75.75 0 10-1.498-.058l-.347 9a.75.75 0 001.5.058l.345-9z"
                                clip-rule="evenodd"></path>
                        </svg>
                    </button>
                    <button @click="() => {
                        selectedTask = task
                        isModifyTaskModalOpen = true
                    }" title="edit task"
                        class="transition w-7 sm:w-8 h-6 sm:h-8 grid place-items-center dark:hover:text-slate-200 hover:text-slate-700">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 512" fill="currentColor"
                            class="w-4 sm:w-5 h-4 sm:h-5">
                            <path
                                d="M64 360c30.9 0 56 25.1 56 56s-25.1 56-56 56s-56-25.1-56-56s25.1-56 56-56zm0-160c30.9 0 56 25.1 56 56s-25.1 56-56 56s-56-25.1-56-56s25.1-56 56-56zM120 96c0 30.9-25.1 56-56 56S8 126.9 8 96S33.1 40 64 40s56 25.1 56 56z">
                            </path>
                        </svg>
                    </button>
                </div>
            </article>
        </li>
    </ul>
    <TaskDetailModal v-if="isModifyTaskModalOpen" :close-modal="() => isModifyTaskModalOpen = false"
        :group-id="props.groupId" :task="selectedTask!"></TaskDetailModal>
</template>

<style scoped></style>

<script setup lang="ts">
import { useStore } from "../state"
import { TaskStatus } from "../service/request_types"
import ChatRoom from "./ChatRoom.vue"
import UserList from "./UserList.vue"
import { ref } from "vue";
const props = defineProps<{
    groupId: number,
}>()

const store = useStore()
const isChatRoomOpen = ref(false)

</script>
<template>
    <div class="bg-slate-100 h-screen w-60 xl:w-2/12 fixed dark:bg-slate-800 z-20 top-0 right-0  block">
        <section class="p-5 flex flex-col h-full">
            <span class="flex items-center mx-auto">
                <span class="font-medium">
                    {{ store.getters.userName }}
                </span>
            </span>
            <div class="mt-6">
                <span class="flex justify-between mb-2">
                    <span>All tasks </span>{{ store.getters.groupTasks(props.groupId).length }}
                </span>
                <span class="flex justify-between ml-2.5 text-sm text-teal-600">
                    <span>Completed tasks </span>{{ store.getters.groupTasksNumber(props.groupId, TaskStatus.Finished) }}
                </span>
                <span class="flex justify-between ml-2.5 text-sm text-sky-500">
                    <span>InProgress tasks </span>{{ store.getters.groupTasksNumber(props.groupId, TaskStatus.InProgress) }}
                </span>
            </div>
            <span @click="() => isChatRoomOpen = true" class="cursor-pointer hover:text-rose-600 mt-6 block pt-4 border-t-slate-200 dark:border-t-slate-700/[.3] border-t-2">
                Open Chat Room
            </span>
            <UserList :group-id="props.groupId"></UserList>
        </section>
    </div>
    <ChatRoom v-if="isChatRoomOpen" :close-modal="() => isChatRoomOpen = false" :group-id="groupId"></ChatRoom>

</template>

<style scoped></style>

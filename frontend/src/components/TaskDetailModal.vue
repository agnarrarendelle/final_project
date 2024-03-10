<script setup lang="ts">
import { ref } from "vue";
import DatePicker from "vue3-datepicker"
import { TaskBody, TaskPriorityLevel, TaskStatus } from "../service/request_types";
import { useStore } from "../state"
import { FwbModal } from "flowbite-vue"
import { modifyGroupTask } from "../service/api/group";
import { TaskResponse, TaskWsResponse } from "../service/response_types";

const props = defineProps<{
    closeModal: () => void
    groupId: number,
    task: TaskResponse
}
>()
const store = useStore()
const task = store.getters.groupTask(props.groupId, props.task.id)

const taskName = ref(task.name)
const taskPriorityLevel = ref(TaskPriorityLevel.Medium)
const taskExpiredAt = ref(new Date(task.expiredAt));

const onFormSubmit = async () => {

    const body: TaskBody = {
        name: taskName.value,
        status: task.status,
        priorityLevel: taskPriorityLevel.value,
        expiredAt: taskExpiredAt.value
    }

    const res = await modifyGroupTask(props.groupId, props.task.id, body)

    const websocket = store.getters.wsClient!;

    const wsBody: TaskWsResponse = {
        type: "Modified",
        task: res.data
    }
    websocket.publish({
        destination: `/app/group/${props.groupId}/task/${res.data.id}`,
        body: JSON.stringify(wsBody)
    })

    props.closeModal()
}
</script>
<template>
    <FwbModal size="5xl" @close="props.closeModal">
        <template #header>
            <h1 class="font-medium mb-5 text-lg md:text-2xl">Edit Task</h1>
        </template>
        <template #body>
            <form class="flex flex-col stylesInputsField">
                <label>Task Name
                    <input v-model="taskName" type="text" placeholder="e.g, study for the test" :required="true"
                        class="w-full">
                </label>
                <label v-if="props.task.status === TaskStatus.InProgress">Date
                    <DatePicker v-model="taskExpiredAt" />
                </label>
                <label>
                    Select a Priority Level
                    <select :required="true" v-model="taskPriorityLevel" class="block w-full">
                        <option v-for="p in TaskPriorityLevel" :value="p" class="bg-slate-100 dark:bg-slate-800">
                            {{ p }}
                        </option>
                    </select>
                </label>
                <button @click="onFormSubmit" type="submit"
                    class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full my-0.5">
                    Submit
                </button>
            </form>
        </template>
    </FwbModal>
</template>
<style scoped></style>

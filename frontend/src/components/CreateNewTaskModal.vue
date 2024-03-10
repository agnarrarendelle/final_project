<script setup lang="ts">
import { ref } from 'vue';
import { FwbModal } from "flowbite-vue"
import { TaskPriorityLevel, TaskStatus, } from '../service/request_types';
import DatePicker from "vue3-datepicker"
import { addGroupTask } from '../service/api/group';
import { useStore } from "../state"
import { TaskWsResponse } from '../service/response_types';

const store = useStore()

const props = defineProps<{
    closeModal: () => void
    groupId: number,
}>()

const taskName = ref("")
const taskPriorityLevel = ref(TaskPriorityLevel.Medium)
const taskCategoryId = ref<number | null>(0)
const taskExpiredAt = ref(new Date());

const onFormSubmit = async () => {
    if (!taskCategoryId.value)
        return
    if (taskName.value.trim().length === 0)
        return

    const res = await addGroupTask(props.groupId, taskCategoryId.value, {
        name: taskName.value,
        status: TaskStatus.InProgress,
        priorityLevel: taskPriorityLevel.value,
        expiredAt: taskExpiredAt.value
    })

    const websocket = store.getters.wsClient!;

    const body: TaskWsResponse = {
        type: "Created",
        task: res.data
    }

    websocket.publish({
        destination: `/app/group/${props.groupId}/task/${res.data.id}`,
        body: JSON.stringify(body)
    })

    props.closeModal()
}


</script>
<template>
    <FwbModal @close="props.closeModal">
        <template #header>
            <h1 class="font-medium mb-5 text-lg md:text-2xl">Create New Task</h1>
        </template>
        <template #body>
            <form class="flex flex-col stylesInputsField">
                <label>Task Name
                    <input v-model="taskName" type="text" placeholder="e.g, study for the test" :required="true"
                        class="w-full">
                </label>
                <label>Date
                    <DatePicker v-model="taskExpiredAt" />
                </label>
                <label>
                    Select a Category
                    <select :required="true" v-model="taskCategoryId" class="block w-full">
                        <option v-for="c in store.getters.groupCategories(props.groupId)" :value="c.id"
                            class="bg-slate-100 dark:bg-slate-800">
                            {{ c.name }}
                        </option>
                    </select>
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
                    Add a task
                </button>
            </form>
        </template>
    </FwbModal>
</template>

<style scoped></style>

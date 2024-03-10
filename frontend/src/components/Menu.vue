<script setup lang="ts">
import { ref } from 'vue';
import { useStore } from '../state';
import { FilterTaskOptionByTaskStatus } from '../service/response_types';
import { MutationTypes } from '../state/mutation-types';
import CreateNewTaskModalVue from './CreateNewTaskModal.vue';
import CreateNewCategoryModal from './CreateNewCategoryModal.vue';

const props = defineProps<{
    groupId: number
}>()

const store = useStore()

const selectedFilterTaskOptionByTaskStatus = ref<FilterTaskOptionByTaskStatus>(FilterTaskOptionByTaskStatus.AllTask)
const selectedFilterTaskOptionByCategoryName = ref("")

const isNewTaskModalOpen = ref(false)

const isNewCategoryModalOpen = ref(false)

const filterTask = () => {

    store.commit(MutationTypes.SET_SORT_AND_FILTER_OPTION, {
        groupId: props.groupId, options: {
            taskStatus: selectedFilterTaskOptionByTaskStatus.value,
            categoryName: selectedFilterTaskOptionByCategoryName.value
        }
    })
}


</script>
<template>
    <div class="bg-slate-100 h-screen w-60 xl:w-2/12 fixed dark:bg-slate-800 z-20 left-0 block">
        <header class="h-full flex flex-col">
            <h1 class="font-bold uppercase text-center mt-8 text-lg tracking-wide hidden xl:block">
                {{ store.getters.groupName(props.groupId) }}</h1>
            <button @click="() => isNewTaskModalOpen = true"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full my-8 mx-4">Add new
                task</button>
            <nav>
                <ul class="grid gap-2">
                    <li @click="() => {
                        selectedFilterTaskOptionByTaskStatus = taskOption
                        filterTask()
                    }
                        " v-for="taskOption in FilterTaskOptionByTaskStatus">
                        <a
                            :class="`${taskOption === selectedFilterTaskOptionByTaskStatus ? 'text-rose-600' : ' '} cursor-pointer px-4 py-2 w-full block transition hover:text-rose-600 dark:hover:text-slate-200 `">
                            {{ taskOption }}
                        </a>
                    </li>
                </ul>
            </nav>
            <div class="py-4">
                <button class="flex items-center w-full mx-4 mb-2 dark:text-slate-200">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" fill="currentColor"
                        class="w-3 h-3 mr-2 rotate-90 transition rotate-180">
                        <path
                            d="M201.4 137.4c12.5-12.5 32.8-12.5 45.3 0l160 160c12.5 12.5 12.5 32.8 0 45.3s-32.8 12.5-45.3 0L224 205.3 86.6 342.6c-12.5 12.5-32.8 12.5-45.3 0s-12.5-32.8 0-45.3l160-160z">
                        </path>
                    </svg>
                    Categories
                </button>
                <div class="visible">
                    <ul class="max-h-72 overflow-auto">
                        <li @click="() => {
                            selectedFilterTaskOptionByCategoryName = ''
                            filterTask()
                        }" class="flex items-center pr-4 pl-9 py-2 itemDirectory hover:cursor-pointer">
                            <div>
                                All Category
                            </div>
                        </li>
                        <li @click="() => {
                            selectedFilterTaskOptionByCategoryName = c.name
                            filterTask()
                        }" v-for="c in store.getters.groupCategories(props.groupId)"
                            class="flex items-center pr-4 pl-9 py-2 itemDirectory cursor-pointer">
                            <div
                                :class="`${c.name === selectedFilterTaskOptionByCategoryName ? 'text-rose-600' : ' '} hover:text-rose-600 dark:hover:text-slate-200 transition text-ellipsis whitespace-nowrap overflow-hidden max-w-[7rem]`">
                                {{ c.name }}
                            </div>
                        </li>
                    </ul>
                    <button
                        class="px-3 py-1 border-slate-300 dark:border-slate-700 border-2 ml-9 mt-2 rounded-md border-dashed hover:text-violet-500"
                        @click="() => isNewCategoryModalOpen = true">
                        + Category
                    </button>
                </div>
            </div>
        </header>
    </div>
    <CreateNewTaskModalVue v-if="isNewTaskModalOpen" :group-id="props.groupId"
        :close-modal="() => isNewTaskModalOpen = false">
    </CreateNewTaskModalVue>
    <CreateNewCategoryModal v-if="isNewCategoryModalOpen" :group-id="props.groupId"
        :close-modal="() => isNewCategoryModalOpen = false">
    </CreateNewCategoryModal>
</template>

<style scoped></style>

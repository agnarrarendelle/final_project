<script setup lang="ts">
import TasksListVue from './TasksList.vue';
import SearchBarVue from './SearchBar.vue';
import { useStore } from "../state"
import { SortTaskOption, TaskResponse } from '../service/response_types';
import { MutationTypes } from '../state/mutation-types';
import { ref } from 'vue';

const props = defineProps<{
    groupId: number,
    taskList: TaskResponse[]
}>()

const store = useStore()

const selectedSortTaskOption = ref<SortTaskOption>(SortTaskOption.Priority)

const changeSortByOption = () => {
    store.commit(MutationTypes.SET_SORT_AND_FILTER_OPTION, {
        groupId: props.groupId, options: {
            sortOption: selectedSortTaskOption.value
        }
    })
}

</script>
<template>
    <main class=" pt-5 pb-8 sm:pb-16 px-3 md:px-8 md:w-full xl:w-8/12 m-auto min-h-screen">
        <header class="items-center grid grid-cols-[1fr_auto_1fr] gap-4 md:gap-0 md:flex ">
            <button class="mr-6 block xl:hidden" title="open menu"><svg xmlns="http://www.w3.org/2000/svg" fill="none"
                    viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 9h16.5m-16.5 6.75h16.5"></path>
                </svg>
            </button>
            <SearchBarVue :group-id="props.groupId"></SearchBarVue>
        </header>
        <section>
            <h1 class="font-medium my-5 text-center sm:text-left sm:my-8 md:text-2xl text-lg dark:text-slate-200">Tasks
            </h1>
            <div class="flex children-styles"><button title="view in list"><svg xmlns="http://www.w3.org/2000/svg"
                        fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="">
                        <path stroke-linecap="round" stroke-linejoin="round"
                            d="M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zM3.75 12h.007v.008H3.75V12zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0z">
                        </path>
                    </svg></button><button title="view in grid"><svg xmlns="http://www.w3.org/2000/svg" fill="none"
                        viewBox="0 0 24 24" stroke-width="{1.5}" stroke="currentColor" class="text-violet-600">
                        <path stroke-linecap="round" stroke-linejoin="round"
                            d="M3.75 6A2.25 2.25 0 016 3.75h2.25A2.25 2.25 0 0110.5 6v2.25a2.25 2.25 0 01-2.25 2.25H6a2.25 2.25 0 01-2.25-2.25V6zM3.75 15.75A2.25 2.25 0 016 13.5h2.25a2.25 2.25 0 012.25 2.25V18a2.25 2.25 0 01-2.25 2.25H6A2.25 2.25 0 013.75 18v-2.25zM13.5 6a2.25 2.25 0 012.25-2.25H18A2.25 2.25 0 0120.25 6v2.25A2.25 2.25 0 0118 10.5h-2.25a2.25 2.25 0 01-2.25-2.25V6zM13.5 15.75a2.25 2.25 0 012.25-2.25H18a2.25 2.25 0 012.25 2.25V18A2.25 2.25 0 0118 20.25h-2.25A2.25 2.25 0 0113.5 18v-2.25z">
                        </path>
                    </svg></button>
                <select v-model="selectedSortTaskOption" @change="changeSortByOption" class="ml-auto inputStyles">
                    <option value="" :disabled="true">Sort by</option>
                    <option :value="sortOption" v-for="sortOption in SortTaskOption"
                        class="bg-slate-100 dark:bg-slate-800">
                        {{ sortOption }}</option>
                </select>
            </div>
            <TasksListVue :group-id="props.groupId" :task-list="props.taskList"></TasksListVue>
        </section>
    </main>
</template>

<style scoped></style>

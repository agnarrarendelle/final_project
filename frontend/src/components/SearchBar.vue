<script setup lang="ts">
import { ref } from 'vue';
import { useStore } from "../state"
import { MutationTypes } from '../state/mutation-types';

const props = defineProps<{
    groupId: number
}>()

const store = useStore()

const query = ref("")

const onSearchBarSubmitted = () => {
    store.commit(MutationTypes.SET_SORT_AND_FILTER_OPTION, {
        groupId: props.groupId, options: {
            searchName: query.value.trim() === "" ? " " : query.value
        }
    })
}


</script>
<template>
    <div class="flex-1 col-span-3 row-start-2 md:pr-10">
        <form class=" relative md:max-w-xs w-full" autocomplete="off">
            <label for="search" class="sr-only"></label>
            <input v-model="query" type="search" id="search" placeholder="Search task" class="inputStyles w-full">
            <svg @click="onSearchBarSubmitted" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                stroke-width="1.5" stroke="currentColor" class="absolute w-4 sm:w-5 right-4 top-3.5 text-slate-400">
                <path stroke-linecap="round" stroke-linejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"></path>
            </svg>
        </form>
    </div>
</template>

<style scoped></style>

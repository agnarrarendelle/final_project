<script setup lang="ts">
import { ref } from "vue"
import { useStore } from '../state';
import InviteNewUserModal from "../components/InviteNewUserModal.vue"
const store = useStore()

const props = defineProps<{
    groupId: number
}>()

const isModalOpen = ref(false)

</script>
<template>
    <h1 class="mt-14">Group Members</h1>
    <ul class="list-none max-h-56">
        <li v-for="u in store.getters.groupUsers(props.groupId)" class="flex items-center  px-6">
            <div class="m-3 flex h-5 w-5 items-center justify-center rounded-full bg-sky-200 ">
                <p>{{ u.name[0] }}</p>
            </div>
            <div class=" flex-1">
                <h3 class=" ml-5 text-sm font-medium text-gray-800">{{ u.name }}</h3>
            </div>
        </li>
    </ul>
    <button @click="() => isModalOpen = true"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full my-8 mx-4">
        Add New User
    </button>
    <InviteNewUserModal v-if="isModalOpen" :group-id="groupId" :close-modal="() => isModalOpen = false">
    </InviteNewUserModal>
</template>

<style scoped></style>


<script setup lang="ts">
import { FwbModal } from "flowbite-vue"
import { ref, reactive } from "vue"
import { UserResponse } from '../service/response_types';
import { searchUsers } from '../service/api/user';
import { addGroupUser } from "../service/api/group";
import { useStore } from "../state";
import { MutationTypes } from "../state/mutation-types";

const store = useStore()

const props = defineProps<{
    groupId: number,
    closeModal: () => void
}>()

const searchResult = reactive<UserResponse[]>([])
const searchTerm = ref("")

const onSearchButtonClicked = async () => {
    if (searchTerm.value.trim().length === 0)
        return

    const res = await searchUsers(props.groupId, searchTerm.value)
    searchResult.length = 0
    searchResult.push(...res.data)

}

const onFormSubmit = async (userId: number) => {
    const res = await addGroupUser(props.groupId, userId)
    store.commit(MutationTypes.ADD_GROUP_USER, { groupId: props.groupId, user: res.data })
    props.closeModal()
}

</script>
<template>
    <FwbModal @close="props.closeModal">
        <template #header>
            <form class="w-full flex flex-col py-5 px-8 rounded-lg" action="">
                <label class="text-gray-700 font-bold py-2" for="">
                    Search User
                </label>
                <input v-model="searchTerm"
                    class="text-gray-700 shadow border rounded border-gray-300 focus:outline-none focus:shadow-outline py-1 px-3 mb-3"
                    type="text" placeholder="Search User">
                <div class="flex justify-between items-center my-4">
                    <button @click="onSearchButtonClicked"
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold rounded py-2 px-4">
                        Search
                    </button>
                </div>
            </form>
        </template>
        <template #body>
            <ul class="list-none max-h-56">
                <li v-for="(user, index) in searchResult" class="flex items-center py-4 px-6">
                    <span class="text-gray-700 text-lg font-medium mr-4">{{ index + 1 }}</span>
                    <h3 class="mr-8 object-cover h-8 w-8 rounded-full bg-sky-200 text-center text-gray-400">
                        {{ user.name[0] }}
                    </h3>
                    <div class="flex-1">
                        <h3 class="text-lg font-medium text-gray-800">{{ user.name }}</h3>
                        <p class="text-gray-600 text-base">id: {{ user.id }}</p>
                    </div>
                    <button @click="onFormSubmit(user.id)"
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold rounded py-2 px-4">
                        Add
                    </button>
                </li>
            </ul>
        </template>
    </FwbModal>
</template>

<style scoped></style>

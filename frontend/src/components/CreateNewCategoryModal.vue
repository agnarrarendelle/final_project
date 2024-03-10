<script setup lang="ts">
import { ref } from 'vue';
import { addGroupCategory } from '../service/api/group';
import { FwbModal } from "flowbite-vue"
import { useStore } from '../state';
import { MutationTypes } from '../state/mutation-types';

const store = useStore()

const props = defineProps<{
    closeModal: () => void
    groupId: number
}>()

const newCategoryName = ref("")

const onFormSubmit = async () => {
    if (newCategoryName.value.trim().length === 0)
        return

    const res = await addGroupCategory(props.groupId, newCategoryName.value)
    store.commit(MutationTypes.ADD_GROUP_CATEGORY, { groupId: props.groupId, category: res.data })
    props.closeModal()
}


</script>
<template>
    <FwbModal @close="props.closeModal">
        <template #header>
            <h1>Create new category</h1>
        </template>
        <template #body>
            <form class="w-full flex flex-col py-5 px-8 rounded-lg" action="">
                <label class="text-gray-700 font-bold py-2" for="">
                    Category Name
                </label>
                <input v-model="newCategoryName"
                    class="text-gray-700 shadow border rounded border-gray-300 focus:outline-none focus:shadow-outline py-1 px-3 mb-3"
                    type="text" placeholder="Category Name">
                <div class="flex justify-between items-center my-4">
                    <button @click="onFormSubmit"
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold rounded py-2 px-4">
                        Submit
                    </button>
                </div>
            </form>
        </template>
    </FwbModal>
</template>

<style scoped></style>

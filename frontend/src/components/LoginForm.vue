<script setup lang="ts">
import { ref } from "vue"
import { useStore } from "../state"
import { login } from '../service/api/user';
import { MutationTypes } from '../state/mutation-types';
import router from '../router';

const store = useStore()

const name = ref("")
const password = ref("")

const onFormSubmit = async () => {
    if (name.value.trim().length === 0 || password.value.trim().length === 0)
        return

    try {
        const res = await login(name.value, password.value)
        store.commit(MutationTypes.SET_USER, res.data)
        store.commit(MutationTypes.INIT_WS_CLIENT)
        router.push({ name: "groups" })
    } catch (_) {
        alert("Failed to login")
    }
}

</script>
<template>
    <form class="w-6/12 mt-36 mx-auto flex flex-col py-5 px-8 rounded-lg" action="">
        <label class="text-gray-700 font-bold py-2" for="">
            Name
        </label>
        <input v-model="name"
            class="text-gray-700 shadow border rounded border-gray-300 focus:outline-none focus:shadow-outline py-1 px-3 mb-3"
            type="text" placeholder="Name">
        <label class="text-gray-700 font-bold py-2" for="">
            Password
        </label>
        <input v-model="password"
            class="text-gray-700 shadow border rounded border-gray-300 focus:outline-none focus:shadow-outline py-1 px-3 mb-3"
            type="password" placeholder="Password">
        <div class="flex justify-between items-center my-4">
            <button @click="onFormSubmit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold rounded py-2 px-4">
                Submit
            </button>
        </div>
    </form>
</template>

<style scoped></style>

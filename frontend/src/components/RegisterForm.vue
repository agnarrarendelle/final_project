<script setup lang="ts">
import { ref } from "vue"
import { register } from '../service/api/user';

const props = defineProps<{
    flipModal: () => void
}>()

const name = ref("")
const email = ref("")
const password = ref("")



const onFormSubmit = async () => {
    if (name.value.trim().length === 0 ||
        password.value.trim().length === 0 ||
        email.value.trim().length === 0
    ) return

    try {
        await register(name.value, email.value, password.value);
    } catch (_) {
        alert("Username or Email taken. Please change them")
        return
    }

    alert("Register successed!!")

    props.flipModal()
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
            Email
        </label>
        <input v-model="email"
            class="text-gray-700 shadow border rounded border-gray-300 focus:outline-none focus:shadow-outline py-1 px-3 mb-3"
            type="text" placeholder="Email">
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

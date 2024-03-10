<script setup lang="ts">
import { ref } from 'vue';
import { FwbButton } from "flowbite-vue"

import LoginForm from "../components/LoginForm.vue"
import RegisterForm from "../components/RegisterForm.vue"
const isSwitched = ref(true)
</script>
<template>
    <LoginForm v-if="isSwitched"></LoginForm>
    <RegisterForm v-else :flip-modal="() => isSwitched = true"></RegisterForm>

    <FwbButton @click="isSwitched = !isSwitched">{{ isSwitched ? "Register" : "Login" }}</FwbButton>
</template>

<style scoped></style>

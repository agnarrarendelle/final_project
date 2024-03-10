<script setup lang="ts">

import { ref } from 'vue';
import { useStore } from '../state';
import { FwbInput, FwbModal } from 'flowbite-vue';
import { ChatMessage } from '../service/request_types';


const store = useStore()

const props = defineProps<{
    groupId: number,
    closeModal: () => void
}>()

const message = ref("")

const onInputSubmitted = () => {
    const wsClient = store.getters.wsClient
    if (wsClient === null) {
        console.error("ws not connected")
        return
    }
    console.log(message)
    const body: ChatMessage = {
        message: message.value,
        userId: store.getters.userId,
        userName: store.getters.userName
    }
    wsClient.publish({
        destination: `/app/group/${props.groupId}`,
        body: JSON.stringify(body)
    })
}
</script>
<template>
    <FwbModal size="5xl" @close="props.closeModal">
        <template #header>
            <h1>Group chat</h1>
        </template>
        <template #body>
            <div class="flex-1 overflow-y-auto p-4 max-h-80">
                <div class="flex flex-col space-y-2">
                    <template v-for="msg in store.getters.groupChatMessages(props.groupId)">
                        <div>
                            <div v-if="store.getters.userId === msg.userId" class="flex justify-end mb-4">
                                <div
                                    class="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white">
                                    {{ msg.message }}
                                </div>
                                <h3 class=" object-cover h-8 w-8 rounded-full bg-sky-200 text-center text-gray-400">
                                    {{ msg.userName[0] }}
                                </h3>
                            </div>
                            <div v-else class="flex justify-start mb-4">
                                <h3 class=" object-cover h-8 w-8 rounded-full bg-sky-200 text-center text-gray-400">
                                    {{ msg.userName[0] }}
                                </h3>
                                <div
                                    class="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white">
                                    {{ msg.message }}
                                </div>
                            </div>
                        </div>
                    </template>
                </div>
            </div>
            <div class="bg-white p-4 flex items-center">
                <FwbInput v-model="message" type="text" placeholder="Type your message..."
                    class="flex-1 border rounded-full px-4 py-2 focus:outline-none"></FwbInput>
                <button @click="onInputSubmitted"
                    class="bg-blue-500 text-white rounded-full p-2 ml-2 hover:bg-blue-600 focus:outline-none">
                    <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
                        stroke="#ffffff">
                        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                        <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                        <g id="SVGRepo_iconCarrier">
                            <path
                                d="M11.5003 12H5.41872M5.24634 12.7972L4.24158 15.7986C3.69128 17.4424 3.41613 18.2643 3.61359 18.7704C3.78506 19.21 4.15335 19.5432 4.6078 19.6701C5.13111 19.8161 5.92151 19.4604 7.50231 18.7491L17.6367 14.1886C19.1797 13.4942 19.9512 13.1471 20.1896 12.6648C20.3968 12.2458 20.3968 11.7541 20.1896 11.3351C19.9512 10.8529 19.1797 10.5057 17.6367 9.81135L7.48483 5.24303C5.90879 4.53382 5.12078 4.17921 4.59799 4.32468C4.14397 4.45101 3.77572 4.78336 3.60365 5.22209C3.40551 5.72728 3.67772 6.54741 4.22215 8.18767L5.24829 11.2793C5.34179 11.561 5.38855 11.7019 5.407 11.8459C5.42338 11.9738 5.42321 12.1032 5.40651 12.231C5.38768 12.375 5.34057 12.5157 5.24634 12.7972Z"
                                stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                        </g>
                    </svg>
                </button>
            </div>
        </template>
    </FwbModal>
</template>

<style scoped></style>

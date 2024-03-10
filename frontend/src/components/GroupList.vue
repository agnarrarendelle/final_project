<script setup lang="ts">
import { useStore } from "../state"

const store = useStore()

</script>
<template>
    <div class="container mx-auto">
        <ul class="py-20">
            <div class="flex items-center px-5 py-2">
                <span class="w-1/6 text-center">
                </span>
                <span class="w-1/2">
                    <span class="text-xs uppercase text-gray-600 font-bold">
                        Group Name
                    </span>
                </span>
            </div>
            <li v-for="(group, index) in [...store.getters.groups].sort((a, b) => a.name.localeCompare(b.name))">
                <RouterLink :to="{ path: `/group/${group.id}` }">
                    <div class="hover:bg-gray-200 cursor-pointer bg-white shadow flex p-5 items-center mb-5 rounded-lg">
                        <div class="w-1/6 text-center">{{ index + 1 }}</div>
                        <div class="w-1/2">
                            <div class="flex items-center">
                                <div class="ml-4">
                                    <span class="capitalize block text-gray-800">
                                        {{ group.name }}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </RouterLink>
            </li>
        </ul>
    </div>
</template>

<style scoped></style>

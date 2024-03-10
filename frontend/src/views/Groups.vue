<script setup lang="ts">
import { onMounted, ref } from 'vue';
import GroupList from '../components/GroupList.vue';
import { getGroups } from '../service/api/group';
import { FwbButton } from 'flowbite-vue'
import CreateNewGroupModalVue from '../components/CreateNewGroupModal.vue';
import { useStore } from '../state';
import { MutationTypes } from '../state/mutation-types';

const store = useStore()

const isModalOpen = ref(false)

onMounted(async () => {
    const res = await getGroups()
    for (const g of res.data) {
        store.commit(MutationTypes.INIT_GROUP_DETAILS, {groupId:g.id, name:g.name})
    }
})

</script>
<template>
    <GroupList ></GroupList>
    <FwbButton @click="() => isModalOpen = true">Create new group</FwbButton>
    <CreateNewGroupModalVue v-if="isModalOpen" :close-modal="() => isModalOpen = false">
    </CreateNewGroupModalVue>
</template>

<style scoped></style>

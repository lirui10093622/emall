<script setup lang="ts">
import axios from 'axios'
import { ref, watchEffect } from 'vue'
const value = ref('')
watchEffect(() => {
  console.log('value', value.value)
})
function onSearch(value: string, e: MouseEvent | KeyboardEvent) {
  console.log('search', value, e)
  axios.get('').then(function (response) {
    
  });
}
</script>
<template>
  <Space vertical>
    <InputSearch
      v-model:value="value"
      placeholder="input search text"
      :icon="false"
      search="Search"
      :search-props="{ type: 'primary' }"
      @search="onSearch"
    />
  </Space>
</template>
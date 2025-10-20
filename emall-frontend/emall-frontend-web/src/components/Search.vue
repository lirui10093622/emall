<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { SearchOutlined, CompassOutlined } from '@ant-design/icons-vue'
const value = ref('')
watchEffect(() => {
  console.log('value', value.value)
})
function onSearch(value: string, e: MouseEvent | KeyboardEvent) {
  console.log('search', value, e)
}
</script>
<template>
  <Space vertical>
    <InputSearch
      v-model:value="value"
      :search-props="{ type: 'primary' }"
      placeholder="input search text"
      @search="onSearch"
    />
    <InputSearch
      v-model:value="value"
      placeholder="input search text"
      :icon="false"
      search="Search"
      :search-props="{ type: 'primary' }"
      @search="onSearch"
    />
    <InputSearch v-model:value="value" placeholder="input search text" @search="onSearch">
      <template #search>
        <Button type="primary">
          <template #icon>
            <SearchOutlined />
          </template>
          Search
        </Button>
      </template>
    </InputSearch>
    <InputSearch
      v-model:value="value"
      placeholder="input search text"
      search="Search"
      :search-props="{ type: 'primary', ghost: true }"
      @search="onSearch"
    >
      <template #icon>
        <CompassOutlined />
      </template>
    </InputSearch>
    <InputSearch v-model:value="value" placeholder="input search text" @search="onSearch">
      <template #search>
        <Button>
          <template #icon>
            <CompassOutlined />
          </template>
          Custom
        </Button>
      </template>
    </InputSearch>
  </Space>
</template>
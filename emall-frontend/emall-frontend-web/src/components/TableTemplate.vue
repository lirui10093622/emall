<script setup lang="ts">
import { ref, reactive, onBeforeMount } from 'vue'
import { SmileOutlined } from '@ant-design/icons-vue'
import type { TableColumn } from 'vue-amazing-ui'
const loading = ref<boolean>(false)
const queryParams = reactive({
  pageSize: 10,
  page: 1
})
const columns = reactive<TableColumn[]>([
  {
    title: 'Name',
    width: 100,
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: 'Age',
    width: 60,
    dataIndex: 'age'
  },
  {
    title: 'Job',
    width: 80,
    dataIndex: 'job',
    key: 'job'
  },
  {
    title: 'Sex',
    width: 60,
    dataIndex: 'sex',
    key: 'sex'
  },
  {
    title: 'Address',
    width: 120,
    dataIndex: 'address'
  },
  {
    title: 'Action',
    width: 150,
    key: 'action'
  }
])
const dataSource = ref([
  {
    name: 'Stephen Curry',
    age: 30,
    job: 'Player',
    sex: 'boy',
    address: 'Chase Center, San Francisco, California'
  },
  {
    name: 'the Muse Catcher',
    age: 24,
    job: 'None',
    sex: 'boy',
    address: 'Beijing, China'
  },
  {
    name: 'Wonder Woman',
    age: 32,
    job: 'Hero',
    sex: 'girl',
    address: 'Tel Aviv, Israel'
  },
  {
    name: 'Superman',
    age: 32,
    job: 'Hero',
    sex: 'boy',
    address: 'United States'
  },
  {
    name: 'Leo',
    age: 36,
    job: 'Actor',
    sex: 'boy',
    address: 'Los Angeles'
  }
])
onBeforeMount(() => {
  getData()
})
function getData() {
  loading.value = true
  // 模拟异步请求获取数据
  setTimeout(() => {
    loading.value = false
  }, 1500)
}
function onChange(page: number, pageSize: number) {
  queryParams.page = page
  queryParams.pageSize = pageSize
  getData()
}
</script>
<template>
  <Table
    :columns="columns"
    :dataSource="dataSource"
    :pagination="{
      showTotal: true
    }"
    :loading="loading"
    @change="onChange"
  >
    <template #headerCell="{ column, title }">
      <template v-if="column.key === 'name'"> <SmileOutlined /> {{ title }} </template>
    </template>
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'name'">
        <a> hello {{ record.name }} </a>
      </template>
      <template v-else-if="column.key === 'sex'">
        <Tag v-if="record.sex === 'boy'" color="volcano">{{ record.sex }}</Tag>
        <Tag v-else-if="record.sex === 'girl'" color="magenta">{{ record.sex }}</Tag>
      </template>
      <template v-else-if="column.key === 'action'">
        <span>
          <a>Invite {{ record.name }}</a>
          <Divider vertical />
          <a>Delete</a>
        </span>
      </template>
    </template>
  </Table>
</template>
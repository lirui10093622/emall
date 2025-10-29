<template>
  <RootLayout>
    <div class="common-layout">
      <el-container>
        <el-header>Header</el-header>
        <el-container>
          <el-aside width="200px">Aside</el-aside>
          <el-main>Main</el-main>
        </el-container>
      </el-container>
    </div>
  </RootLayout>
</template>

<script setup lang="ts">
import RootLayout from './RootLayout.vue';
</script>

<style scoped>
.el-header {
  background-color: red;
  text-align: center;
  line-height: 60px;
  width: 100%;
}

.el-aside {
  background-color: #d3dce6;
  text-align: center;
  height: 100vh;
}
</style>
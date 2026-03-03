<template>
  <div id = "app">
  <nav>
    <router-link to="/login" tag="button" class="btn btn-primary">登录</router-link>
  </nav>
  </div>
  <router-view></router-view>
</template>

<style scoped>
.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.btn-primary {
  background: #42b983;
  color: white;
}

.btn-primary:hover {
  background: #33a06f;
}

.btn-primary.active {
  background: #2c8c5e;
  box-shadow: inset 0 2px 4px rgba(0,0,0,0.1);
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

</style>
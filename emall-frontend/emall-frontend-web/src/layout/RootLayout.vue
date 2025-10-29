<template>
    <!-- 根布局 -->
    <div class="rootLayout">
        <!-- 布局内容插槽 - 各种布局模式的具体内容 -->
        <slot></slot>
    </div>
</template>

<style scoped>
.rootLayout {
    width: 100%;
    height: 100%;
}
</style>
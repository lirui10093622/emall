import { defineConfig } from 'vite'

import vue from '@vitejs/plugin-vue'
import path from 'path'

import Components from 'unplugin-vue-components/vite'
import { VueAmazingUIResolver } from 'vue-amazing-ui'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue(),
    Components({
      resolvers: [
        // auto import components from VueAmazingUI
        VueAmazingUIResolver({
          cjs: false // whether use commonjs build, default false
        })
      ]
    })],
  resolve: {
    alias: {
      // 基础别名
      '@': path.resolve(__dirname, './src'),
      // 常用目录别名
      '@components': path.resolve(__dirname, './src/components'),
      '@views': path.resolve(__dirname, './src/views'),
      '@utils': path.resolve(__dirname, './src/utils'),
      '@api': path.resolve(__dirname, './src/api'),
      '@assets': path.resolve(__dirname, './src/assets'),
      '@stores': path.resolve(__dirname, './src/stores'),
      '@types': path.resolve(__dirname, './src/types'),
      '@styles': path.resolve(__dirname, './src/styles'),
    }
  }
})
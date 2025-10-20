import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
// vue-amazing-ui 按需引入
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
})

import { fileURLToPath, URL } from 'node:url'
import { type ConfigEnv, defineConfig, loadEnv, type UserConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// 引入自动导入插件
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
// 引入svg-icons插件用于注册svg图标
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
// 引入top-level-await插件 允许顶级await
import topLevelAwait from 'vite-plugin-top-level-await'
//Gzip文件压缩
import viteCompression from 'vite-plugin-compression'
// 引入打包分析插件
import { visualizer } from 'rollup-plugin-visualizer'
import path from 'path'

// https://vitejs.awaitdev/config/
export default defineConfig((configEnv: ConfigEnv): UserConfig => {
  // 加载 .env 文件中的变量
  const { mode } = configEnv
  const env = loadEnv(mode, process.cwd(), '')
  // 基础配置对象
  return {
    build: {
      // 清除所有console和debugger
      minify: 'terser',
      terserOptions: {
        compress: {
          drop_console: true,
          drop_debugger: true
        }
      },
      rollupOptions: {
        output: {
          // 引入文件名的名称
          chunkFileNames: 'js/[name]-[hash].js',
          // 包的入口文件名称
          entryFileNames: 'js/[name]-[hash].js',
          // 资源文件像 字体，图片等
          assetFileNames: '[ext]/[name]-[hash].[ext]',
          // 手动指定分块
          manualChunks(id: string) {
            // 检查是否是node_modules中的文件
            if (id.includes('node_modules')) {
              return 'vendor'
            }
          }
        }
      }
    },
    plugins: [
      vue(),
      createSvgIconsPlugin({
        iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
        symbolId: 'svg-[name]'
      }),
      //开启Gzip压缩
      viteCompression({
        // 是否在控制台中输出压缩结果
        verbose: true,
        disable: false,
        // 如果体积大于阈值，将被压缩，单位为b，体积过小时请不要压缩 10kb
        threshold: 10240,
        // 压缩算法，可选['gzip'，' brotliccompress '，'deflate '，'deflateRaw']
        algorithm: 'gzip',
        ext: '.gz',
        // 源文件压缩后是否删除
        deleteOriginFile: false
      }),
      visualizer({ open: true }),
      topLevelAwait(),
      AutoImport({
        imports: ['vue', 'vue-router', 'pinia'],
        resolvers: [ElementPlusResolver()],
        dts: true
      }),
      Components({
        dts: true,
        resolvers: [ElementPlusResolver()]
      })
    ],
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: `@use "@/assets/style/media.scss" as *;`
        }
      }
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    server: {
      proxy: {
        [env.VITE_API_PREFIX]: {
          target: env.VITE_PROXY_URL,
          changeOrigin: true,
          rewrite: (path: string) => path.replace(env.VITE_API_PREFIX, '')
        }
      },
      warmup: {
        clientFiles: ['./index.html', './src/{views,components}/**']
      }
    }
  }
})

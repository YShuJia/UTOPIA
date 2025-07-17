/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

// @see https://eslint.bootcss.com/docs/rules/

module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true,
    jest: true
  },
  /* 指定如何解析语法 */
  parser: 'vue-eslint-parser',
  /** 优先级低于 parse 的语法解析配置 */
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
    parser: '@typescript-eslint/parser',
    jsxPragma: 'React',
    ecmaFeatures: {
      jsx: true
    }
  },
  /* 继承已有的规则 */
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-essential',
    'plugin:@typescript-eslint/recommended',
    '@vue/eslint-config-typescript'
  ],
  plugins: ['vue', '@typescript-eslint'],
  rules: {
    // 要求使用 let 或 const 而不是 var
    'no-var': 'error',
    // 不允许多个空行
    'no-multiple-empty-lines': ['warn', { max: 1 }],
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    // 禁止空余的多行
    'no-unexpected-multiline': 'error',
    // 禁止不必要的转义字符
    'no-useless-escape': 'off',
    // typeScript (https://typescript-eslint.io/rules)
    // 禁止定义未使用的变量
    '@typescript-eslint/no-unused-vars': 'off',
    // 使用 @ts-ignore
    '@typescript-eslint/prefer-ts-expect-error': 'error',
    // 禁止使用 any 类型
    '@typescript-eslint/no-explicit-any': 'off',
    '@typescript-eslint/no-non-null-assertion': 'off',
    // 禁止使用自定义 TypeScript 模块和命名空间。
    '@typescript-eslint/no-namespace': 'off',
    '@typescript-eslint/semi': 'off',

    // eslint-plugin-vue (https://eslint.vuejs.org/rules/)
    // 要求组件名称始终为 “-” 链接的单词
    'vue/multi-word-component-names': 'off',
    // 防止<script setup>使用的变量<template>被标记为未使用
    'vue/script-setup-uses-vars': 'error',
    // 不允许组件 prop的改变
    'vue/no-mutating-props': 'off',
    // 对模板中的自定义组件强制执行属性命名样式
    'vue/attribute-hyphenation': 'off'
  }
}

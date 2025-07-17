<script lang="ts" setup>
defineProps({
  message: String,
  title: String
})

const emit = defineEmits(['close'])

function confirmAction() {
  emit('close', true)
}

function cancelAction() {
  emit('close', false)
}
</script>

<template>
  <el-container class="size-full bg-gray-900/80 items-center justify-center absolute z-[10000]">
    <div class="container-inner p-5 max-w-96 h-fit bg-red-800 radius-lg">
      <div class="content p-5 radius-lg">
        <h3>{{ title }}</h3>
        <p>{{ message }}</p>
      </div>
      <div class="buttons">
        <button class="confirm" type="button" @click="confirmAction">确认</button>
        <button class="cancel" type="button" @click="cancelAction">取消</button>
      </div>
    </div>
  </el-container>
</template>

<!--<style scoped>
/* 根据你的需求保留或修改样式 */
</style>

<template>
  <el-container class="size-full bg-gray-900/80 items-center justify-center absolute z-[10000]">
    <div class="container-inner p-5 max-w-96 h-fit bg-red-800 radius-lg">
      <div class="content p-5 radius-lg">
        <p>Do you want to Continue?</p>
      </div>
      <div class="buttons">
        <button class="confirm" type="button">Confirm</button>
        <button class="cancel" type="button">Cancel</button>
      </div>
    </div>
  </el-container>
</template>-->

<style lang="scss" scoped>
.container-inner {
  box-shadow:
    5px 6px 0px -2px #620d15,
    -6px 5px 0px -2px #620d15,
    0px -2px 0px 2px #ee9191,
    0px 10px 0px #610c14,
    0px -10px 0px 1px #e66565;

  .content {
    background: radial-gradient(#fffbf3, #ffe19e);
    box-shadow:
      0px 0px 0px 6px #5e1e21,
      0px 0px 8px 6px #84222b,
      inset 0px 0px 15px 0px #614506,
      6px 6px 1px 1px #e66565,
      -6px 6px 1px 1px #e66565;

    p {
      color: #461417;
      font-size: 25px;
      font-weight: 600;
      text-align: center;
    }
  }

  .buttons {
    margin-top: 40px;
    display: flex;
    justify-content: center;
    gap: 40px;
  }

  button {
    border-radius: 20px;
    border: 2px solid #49181e;
    color: #fff;
    cursor: pointer;
    font-size: 20px;
    padding-inline: 20px;
    padding-block: 10px;
    text-shadow: 1px 2px 3px #000000;
  }

  .confirm {
    background: linear-gradient(#ced869, #536d1b);
    box-shadow:
      0 0 0 4px #7e1522,
      0 2px 0 3px #e66565;
  }

  .confirm:hover {
    box-shadow:
      0 0 0 4px #7e1522,
      0 2px 0 3px #e66565,
      inset 2px 2px 10px 3px #4e6217;
  }

  .cancel {
    background: linear-gradient(#ea7079, #891a1a);
    box-shadow:
      0 0 0 4px #7e1522,
      0 2px 0 3px #e66565;
  }

  .cancel:hover {
    box-shadow:
      0 0 0 4px #7e1522,
      0 2px 0 3px #e66565,
      inset 2px 2px 10px 3px #822828;
  }
}
</style>

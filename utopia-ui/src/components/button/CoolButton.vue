<script lang="ts" setup>
const { message } = defineProps({
  message: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="container-button">
    <div class="hover bt-1"></div>
    <div class="hover bt-2"></div>
    <div class="hover bt-3"></div>
    <div class="hover bt-4"></div>
    <div class="hover bt-5"></div>
    <div class="hover bt-6"></div>
    <button :data-before="message" class="whitespace-nowrap px-3 py-1.5 radius-lg" />
  </div>
</template>

<style lang="scss" scoped>
.container-button {
  display: grid;
  grid-template-columns: repeat(3, 33.33%);
  grid-template-rows: repeat(2, 50%);
  grid-template-areas:
    'bt-1 bt-2 bt-3'
    'bt-4 bt-5 bt-6';
  position: relative;
  perspective: 800px;
  transition: all 0.3s ease-in-out;

  &:active {
    transform: scale(0.95);
  }
}

.hover {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 200;

  &:hover ~ button::after {
    content: attr(data-before);
    position: absolute;
    top: -135%;
    width: 100%;
    transform: translate(-50%, 0);
    font-size: 25px;
    color: var(--hover-color);
    background-color: transparent;
    font-weight: 900;
    z-index: 200;
  }
}

.bt-1 {
  grid-area: bt-1;
}

.bt-2 {
  grid-area: bt-2;
}

.bt-3 {
  grid-area: bt-3;
}

.bt-4 {
  grid-area: bt-4;
}

.bt-5 {
  grid-area: bt-5;
}

.bt-6 {
  grid-area: bt-6;
}

.bt-1:hover ~ button {
  transform: rotateX(15deg) rotateY(-15deg) rotateZ(0deg);
  box-shadow: -5px -5px var(--shadow-color);

  &::after {
    animation: shake 0.5s ease-in-out 0.3s;
    text-shadow: -5px -5px var(--shadow-color);
  }
}

.bt-3:hover ~ button {
  transform: rotateX(15deg) rotateY(15deg) rotateZ(0deg);
  box-shadow: 5px -5px var(--shadow-color);

  &::after {
    animation: shake 0.5s ease-in-out 0.3s;
    text-shadow: 5px -5px var(--shadow-color);
  }
}

.bt-4:hover ~ button {
  transform: rotateX(-15deg) rotateY(-15deg) rotateZ(0deg);
  box-shadow: -5px 5px var(--shadow-color);

  &::after {
    animation: shake 0.5s ease-in-out 0.3s;
    text-shadow: -5px 5px var(--shadow-color);
  }
}

.bt-6:hover ~ button {
  transform: rotateX(-15deg) rotateY(15deg) rotateZ(0deg);
  box-shadow: 5px 5px var(--shadow-color);

  &::after {
    animation: shake 0.5s ease-in-out 0.3s;
    text-shadow: 5px 5px var(--shadow-color);
  }
}

button {
  /* 从第一列到最后一列 */
  grid-column: 1 / -1;
  /* 从第一行到最后一行 */
  grid-row: 1 / -1;
  width: fit-content;
  position: relative;
  background: transparent;
  border: 3px solid var(--hover-color);
  transition:
    transform 0.3s ease-in-out,
    box-shadow 0.3s ease-in-out;

  &::before {
    color: transparent;
    content: attr(data-before);
    z-index: -9;
  }

  &::after {
    content: attr(data-before);
    transition: all 0.3s ease-in-out;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: -1;
  }
}

@keyframes shake {
  0% {
    left: 45%;
  }

  25% {
    left: 54%;
  }

  50% {
    left: 48%;
  }

  75% {
    left: 52%;
  }

  100% {
    left: 50%;
  }
}
</style>

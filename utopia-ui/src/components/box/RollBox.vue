<script lang="ts" setup>
const { likeCount } = defineProps({
  likeCount: {
    type: Number,
    default: 0
  }
})
</script>

<template>
  <button class="Btn">
    <span class="leftContainer">
      <svg fill="white" height="1em" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
        <path
          d="M47.6 300.4L228.3 469.1c7.5 7 17.4 10.9 27.7 10.9s20.2-3.9 27.7-10.9L464.4 300.4c30.4-28.3 47.6-68 47.6-109.5v-5.8c0-69.9-50.5-129.5-119.4-141C347 36.5 300.6 51.4 268 84L256 96 244 84c-32.6-32.6-79-47.5-124.6-39.9C50.5 55.6 0 115.2 0 185.1v5.8c0 41.5 17.2 81.2 47.6 109.5z"
        ></path>
      </svg>
      <span class="like">Like</span>
    </span>
    <span class="likeCount"> {{ likeCount }} </span>
  </button>
</template>

<style lang="scss" scoped>
.Btn {
  width: 140px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  border: none;
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.089);
  cursor: pointer;
  background-color: transparent;
}

.leftContainer {
  width: 60%;
  height: 100%;
  background-color: rgb(238, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.leftContainer .like {
  color: white;
  font-weight: 600;
}

.likeCount {
  width: 40%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgb(238, 0, 0);
  font-weight: 600;
  position: relative;
  background-color: white;
}

.likeCount::before {
  height: 8px;
  width: 8px;
  position: absolute;
  content: '';
  background-color: rgb(255, 255, 255);
  transform: rotate(45deg);
  left: -4px;
}

.Btn:hover .leftContainer {
  background-color: rgb(219, 0, 0);
}

.Btn:active .leftContainer {
  background-color: rgb(201, 0, 0);
}

.Btn:active .leftContainer svg {
  transform: scale(1.15);
  transform-origin: top;
}
</style>

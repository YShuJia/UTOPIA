<script lang="ts" setup>
const { height, count, value } = defineProps({
  height: {
    type: Number,
    default: 36
  },
  // 父组件不传入自动生成时的验证码个数
  count: {
    type: Number,
    default: 4
  },
  value: {
    type: String,
    default: ''
  }
})

// 绑定验证码
const code = defineModel('code', {
  default: ''
})

const codeArr: string[] = [
  'a',
  'b',
  'c',
  'd',
  'e',
  'f',
  'g',
  'h',
  'j',
  'k',
  'm',
  'n',
  'p',
  'q',
  'r',
  's',
  't',
  'u',
  'v',
  'w',
  'x',
  'y',
  'z',
  '2',
  '3',
  '4',
  '5',
  '6',
  '7',
  '8',
  '9'
]

const draw = (arr: string[]) => {
  //获取到canvas
  const canvas: any = document.getElementById('canvas')
  if (!canvas) {
    return
  }
  // 根据要绘制的数组长度计算宽度（间距 + 字宽）
  const width = 3 * arr.length + 16 * arr.length
  let context = canvas.getContext('2d')
  canvas.width = width
  canvas.height = height
  const halfHeight = height / 2 + 2
  for (let i = 0; i < arr.length; i++) {
    // 产生 0~20 之间的随机弧度
    const random: number = Math.random()
    let deg = ((Math.random() - 0.5) * 30 * Math.PI) / 180
    //文字在 canvas 上的 x 坐标
    let x = 3 + i * 16
    //文字在 canvas 上的 y 坐标
    let y = Math.floor(halfHeight + random * 8)
    context.font = 'bolder 25px 微软雅黑'
    // 旋转画布
    context.translate(x, y)
    context.rotate(deg)
    context.fillStyle = randomColor()
    context.fillText(arr[i], 0, 0)
    // 恢复画布的状态
    context.rotate(-deg)
    context.translate(-x, -y)
  }

  //验证码上显示线条
  for (let i = 0; i <= 5; i++) {
    context.strokeStyle = randomColor()
    context.beginPath()
    context.moveTo(Math.random() * width, Math.random() * height)
    context.lineTo(Math.random() * width, Math.random() * height)
    context.stroke()
  }

  //验证码上显示小点
  for (let i = 0; i <= 20; i++) {
    context.strokeStyle = randomColor()
    context.beginPath()
    let x = Math.random() * width
    let y = Math.random() * height
    context.moveTo(x, y)
    context.lineTo(x + 1, y + 1)
    context.stroke()
  }
}

const randomColor = () => {
  //得到随机的颜色值
  const r = Math.floor(Math.random() * 256)
  const g = Math.floor(Math.random() * 256)
  const b = Math.floor(Math.random() * 256)
  return 'rgb(' + r + ',' + g + ',' + b + ')'
}

// 生成指定长度的验证码
const generateCode = (count: number): string => {
  return Array.from(
    { length: count },
    () => codeArr[Math.floor(Math.random() * codeArr.length)]
  ).join('')
}

const initDraw = () => {
  if (value !== '') {
    draw(Array.from(value))
  } else {
    const showCode = generateCode(count)
    code.value = showCode.toUpperCase()
    draw(Array.from(showCode))
  }
}

const watchValue = watch(
  () => value,
  () => {
    console.log('123')
    initDraw()
  },
  {
    immediate: true
  }
)
onDeactivated(() => {
  watchValue.stop()
})
</script>

<template>
  <a id="canvasWrapper" href="javascript:">
    <canvas id="canvas" />
  </a>
</template>

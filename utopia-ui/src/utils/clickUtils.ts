import mario from '@/assets/img/mario-face.png'
import { ClickEnum } from '@/enum'

export type ClickType =
  | ClickEnum.CHAR
  | ClickEnum.CIRCLE
  | ClickEnum.LINE
  | ClickEnum.LOVE
  | ClickEnum.MARIO
  | ClickEnum.SQUARE

const arrChar = [
  '富强',
  '民主',
  '文明',
  '和谐',
  '自由',
  '平等',
  '公正',
  '法治',
  '爱国',
  '敬业',
  '诚信',
  '友善'
]

export function pop(e: any, type: ClickType = ClickEnum.CHAR, num: number = 20) {
  if (type === 'CHAR') {
    const particle = document.createElement('particle')
    particle.className = 'particle'
    particle.style.top = e.clientY - 16 + 'px'
    particle.style.left = e.clientX - 18 + 'px'
    particle.innerHTML = arrChar[Math.floor(Math.random() * arrChar.length)] ?? ''
    document.body.appendChild(particle)
    // 动画结束 移除元素
    particle.onanimationend = () => {
      particle.remove()
    }
    return
  }

  // 限制num 最小5 最大100
  num = num < 5 ? 5 : num
  num = num > 100 ? 100 : num

  if (e.clientX === 0 && e.clientY === 0) {
    const bbox = e.target.getBoundingClientRect()
    const x = bbox.left + bbox.width / 2
    const y = bbox.top + bbox.height / 2
    for (let i = 0; i < num; i++) {
      createParticle(x, y, type)
    }
  } else {
    for (let i = 0; i < num; i++) {
      createParticle(e.clientX, e.clientY + window.scrollY, type)
    }
  }
}

function createParticle(x: number, y: number, type: string) {
  const particle = document.createElement('particle')
  particle.className = 'particle'
  particle.style.position = 'fixed'
  particle.style.top = '0'
  particle.style.left = '0'
  particle.style.opacity = '0'
  particle.style.zIndex = '999999999'
  document.body.appendChild(particle)
  // 大小
  const size = Math.floor(Math.random() * 20 + 15)
  const width: number = size
  let height: number = size
  // 位置
  const destinationX = (Math.random() - 0.5) * 300
  const destinationY = (Math.random() - 0.5) * 300
  // 旋转
  let rotation = Math.random() * 520
  // 延迟
  const delay = Math.random() * 200
  // 颜色
  const color = `hsl(${Math.random() * 300 + 60}, 70%, 50%)`

  switch (type) {
    case 'SQUARE':
      particle.style.background = `hsl(${Math.random() * 90 + 270}, 70%, 60%)`
      break
    case 'LOVE':
      particle.innerHTML = ['❤', '🧡', '💛', '💚', '💙', '💜'][Math.floor(Math.random() * 6)] ?? ''
      particle.style.fontSize = `${width}px`
      break
    case 'MARIO':
      particle.style.backgroundImage = 'url(' + mario + ')'
      particle.style.backgroundSize = 'cover'
      break
    case 'CIRCLE':
      particle.style.boxShadow = `0 0 ${Math.floor(Math.random() * 10 + 10)}px ${color}`
      particle.style.background = color
      particle.style.borderRadius = '50%'
      break
    case 'LINE':
      particle.style.background = color
      height = 1
      rotation += 1000
      break
  }

  particle.style.width = `${width}px`
  particle.style.height = `${height}px`
  const animation = particle.animate(
    [
      {
        transform: `translate(-50%, -50%) translate(${x}px, ${y}px) rotate(0deg)`,
        opacity: 1
      },
      {
        transform: `translate(-50%, -50%) translate(${x + destinationX}px, ${y + destinationY}px) rotate(${rotation}deg)`,
        opacity: 0
      }
    ],
    {
      duration: Math.random() * 1000 + 5000,
      easing: 'cubic-bezier(0, .9, .57, 1)',
      delay: delay
    }
  )
  // 动画结束 移除元素
  animation.onfinish = () => {
    particle.remove()
  }
}

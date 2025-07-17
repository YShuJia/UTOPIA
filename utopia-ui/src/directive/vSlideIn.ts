import type { DirectiveBinding } from 'vue'

const distance = 0
const duration = 1000

// WeakMap避免内存泄露
const animationMap = new WeakMap()
//监听元素是否与当前页面视口重叠，即判断元素是否出现于当前视口内，返回所有元素监听结果
const ob = new IntersectionObserver((entries) => {
  for (const entry of entries) {
    //判断元素是否在视口内
    if (entry.isIntersecting) {
      //获取当前在视口内的元素动画对象
      const animation = animationMap.get(entry.target)
      //开始执行动画
      animation.play()
      //执行完后取消监听，限制动画只播放一次，防止回滚时重复播放
      ob.unobserve(entry.target)
    }
  }
})

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    // 配置是否开启动画
    const value = binding.value ?? true
    if (!value) {
      return
    }
    const animation = el.animate(
      [
        //配置关键帧
        {
          transform: `translateY(${distance}px)`,
          opacity: 0.1,
          scale: 0.8
        },
        {
          transform: `translateY(0)`,
          opacity: 1,
          scale: 1
        }
      ],
      {
        //配置动画执行时间
        duration: duration,
        //配置动画时间函数
        easing: 'ease-in-out'
      }
    )
    //起始状态暂停动画执行，待元素在视口范围内执行动画
    animation.pause()
    //将元素与对象绑定进行映射
    animationMap.set(el, animation)
    //监听动画是否与视口重叠
    ob.observe(el)
  },
  //元素卸载时取消监听
  unmounted(el: HTMLElement) {
    ob.unobserve(el)
  }
}

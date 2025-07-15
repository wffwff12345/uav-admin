<template>
  <!-- 页面内伸缩层 -->
  <div class="page-drawer-area" ref="pageDrawer">
    <slot></slot>
    <div v-if="showTrigger" class="trigger" @click="triggerHandler"></div>
  </div>
</template>

<script>
/* 页面侧边栏的伸缩层组件，现支持从 左 右 下 方弹出和收缩
本组件本身是绝对定位元素，使用前请记得为需要显示该伸缩层的父元素添加 position: relative 和 overflow: hidden
当然如果你不想手动控制，可以将 mounted 钩子函数中的两步重要处理解除注释，根据你自己的情况选择。

PS：如果不需要使用与伸缩面板联动的触发器trigger，可以使用 vue 自带的 transition 组件，使用更方便快捷
PS2：使用时记得拷贝图包 =^=
by. ww 24.04.22
*/
export default {
  name: 'CustomPageDrawer',
  props: {
    // 控制伸缩层的方向（目前支持 从右、从下 伸出）
    from: {
      type: String,
      required: true,
      default: 'right',
      validator: function (value) {
        return ['right', 'left', 'bottom'].includes(value)
      },
    },
    // 控制伸缩层的大小 width、height接受css支持的所有值，包括 带单位的数值，百分比，auto
    dWidth: {
      type: String,
      default: '',
    },
    dHeight: {
      type: String,
      default: '',
    },
    // 在垂直方向上是否居中
    isCenter: {
      type: Boolean,
      default: false,
    },
    // 与父组件双向绑定的伸缩层状态（绑定添加.sync修饰符）
    openFlag: {
      type: Boolean,
      default: true,
    },
    // 是否显示伸缩按钮触发器
    showTrigger: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      // 打开或关闭的标志
      ifOpen: false,
    }
  },
  watch: {
    openFlag: {
      handler(newval) {
        this.ifOpen = newval
        this.$nextTick(() => {
          if (newval) this.$refs.pageDrawer.classList.add('open')
          else this.$refs.pageDrawer.classList.remove('open')
        })
      },
      immediate: true,
    },
  },
  mounted() {
    // 重要处理1：本组件通过绝对定位实现，为了确保样式不混乱，如果组件父元素不是body且是静态元素，可根据需要要为父元素添加相对定位
    // let parentEle = this.$refs.pageDrawer.parentNode
    // if (parentEle != document.body && window.getComputedStyle(parentEle).position === 'static') {
    //   parentEle.style.position = 'relative'
    // }
    // 重要处理2：为父元素添加 overflow:hidden 保证隐藏效果
    // parentEle.style.overflow = 'hidden'

    // 控制伸缩层的大小
    if (this.dWidth !== '') this.$refs.pageDrawer.style.width = this.dWidth
    if (this.dHeight !== '') this.$refs.pageDrawer.style.height = this.dHeight
    // 根据输入的from控制伸缩层的样式
    if (this.from === 'right') this.$refs.pageDrawer.classList.add('from-right')
    else if (this.from === 'left') this.$refs.pageDrawer.classList.add('from-left')
    else if (this.from === 'bottom') this.$refs.pageDrawer.classList.add('from-bottom')
    // 控制居中
    if (this.isCenter) this.$refs.pageDrawer.classList.add('is-center')

    /* 在首次加载时，transform无论如何都会触发一次，但因为有些数据样式是异步设置的（比如居中），这会导致在首次加载时产生预想之外的移动效果（斜着移动）。
    因此配合css设置初始的 display:none，在所有的样式设置完成后再动态设置 display:block，由此避免诡异的移动效果，同时关闭了加载时的过渡效果。 */
    this.$refs.pageDrawer.style.display = 'block'
  },
  methods: {
    // 伸缩层的展开与隐藏
    triggerHandler() {
      this.ifOpen = !this.ifOpen
      if (this.ifOpen) this.$refs.pageDrawer.classList.add('open')
      else this.$refs.pageDrawer.classList.remove('open')
      this.$emit('update:openFlag', this.ifOpen)
    },
  },
}
</script>

<style lang="scss" scoped>
.page-drawer-area {
  display: none; // 消除加载时的过渡效果
  position: absolute;
  transition: all 1s;
  z-index: 999;
  .trigger {
    position: absolute;
    opacity: 0.8;
    &:hover {
      transform: scale(120%);
      opacity: 1;
    }
  }
  &.from-right {
    right: 0;
    top: 0vh;
    transform: translate(100%, 0);
    width: 20%;
    height: 100%;
    .trigger {
      left: -2.5rem;
      top: 10%;
      width: 2.5rem;
      height: 80%;
      background: url('./images/popup/to-left.png') no-repeat center/contain;
    }
    &.is-center {
      top: 50%;
      transform: translate(100%, -50%);
    }
    &.open {
      transform: translate(0, 0);
      .trigger {
        background: url('./images/popup/to-right.png') no-repeat center/contain;
      }
      &.is-center {
        transform: translate(0, -50%);
      }
    }
  }
  &.from-left {
    left: 0;
    top: 0vh;
    transform: translate(-100%, 0);
    width: 20%;
    height: 100%;
    .trigger {
      right: -2.5rem;
      top: 10%;
      width: 2.5rem;
      height: 80%;
      background: url('./images/popup/to-right.png') no-repeat center/contain;
    }
    &.is-center {
      top: 50%;
      transform: translate(-100%, -50%);
    }
    &.open {
      transform: translate(0, 0);
      .trigger {
        background: url('./images/popup/to-left.png') no-repeat center/contain;
      }
      &.is-center {
        transform: translate(0, -50%);
      }
    }
  }
  &.from-bottom {
    left: 0;
    bottom: 0;
    width: 52%;
    height: 20%;
    .trigger {
      left: 10%;
      top: -2.5rem;
      width: 80%;
      height: 2.5rem;
      background: url('./images/popup/to-top.png') no-repeat center/contain;
    }
    &.is-center {
      left: 50%;
      transform: translate(-50%, 100%);
    }
    &.open {
      transform: translate(0, 0);
      .trigger {
        background: url('./images/popup/to-bottom.png') no-repeat center/contain;
      }
      &.is-center {
        left: 50%;
        transform: translate(-50%, 0);
      }
    }
  }
}
</style>

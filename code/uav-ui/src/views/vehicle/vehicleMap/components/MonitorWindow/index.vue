<template>
  <div class="video-box">
    <video :id="videoID" class="vjs-custom-player video-js" muted preload="auto" autoplay></video>
  </div>
</template>

<script>
// 监控视频组件 解析m3u8 更新 24.04.17
// 调用前安装 npm install video.js --save
import videojs from 'video.js'
import 'video.js/dist/video-js.css'

export default {
  name: 'MonitorWindow',
  props: {
    // 播放器地址
    playerSrc: {
      required: true,
      type: String,
      default: 'xxx',
    },
    // 播放器独立的自定义ID （同一页面多次调用该组件请确保每个组件赋予的该值不相同）
    playerID: {
      required: true,
      type: [String, Number],
      default: '',
    },
  },
  data() {
    return {
      // 播放器实例
      videoToken: null,
    }
  },
  computed: {
    // 播放器的id
    videoID() {
      return 'player' + this.playerID
    },
  },
  mounted() {
    this.videoToken = videojs(this.videoID, {
      notSupportedMessage: '视频播放不正常，请检查播放源。',
    })
  },
  activated() {
    this.videoToken.play() // 组件重启时播放
  },
  deactivated() {
    this.videoToken.pause() // 组件停用时暂停
  },
  beforeDestroy() {
    if (this.videoToken) {
      this.videoToken.pause() // 停止
      this.videoToken.dispose() // 销毁
      this.videoToken = null
    }
  },
  watch: {
    // 更新播放器地址
    playerSrc: {
      handler(newval) {
        this.$nextTick(() => {
          this.startPlayer(newval)
        })
      },
      immediate: true,
    },
  },
  methods: {
    // 根据地址执行播放
    startPlayer(address) {
      this.videoToken.reset() // 重置
      this.videoToken.src({ src: address, type: 'application/x-mpegURL' }) // 动态修改地址
      this.videoToken.ready(() => {
        // 准备播放
        this.videoToken.play().catch(e => {
          console.log('播放器播放不正常', e.message)
        })
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.video-box {
  height: 100%;
  .vjs-custom-player {
    width: 100%;
    height: 100%;
    font-size: 1rem;
  }
}
</style>

<template>
  <div class="map-wrapper">
    <!-- 百度地图vue-baidu-map -->
    <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center" :scroll-wheel-zoom="true" @ready="mapHandler">
      <!-- 比例尺 -->
      <bm-scale anchor="BMAP_ANCHOR_TOP_RIGHT" :offset="controlOffset"></bm-scale>
      <!-- 自定义控件：播放轨迹 -->
      <bm-control anchor="BMAP_ANCHOR_TOP_LEFT" :offset="controlOffset">
        <el-button class="play-lushu" @click="startLushu">轨迹回放</el-button>
      </bm-control>
      <!-- 路书 -->
      <bml-lushu v-if="lushuShow" @stop="resetTrack" :path="currentTrack" :icon="lushuIcon" :play="playback" :speed="1000" :autoView="true"></bml-lushu>
      <!-- 行进轨迹 -->
      <bm-polyline v-for="(path, index) in tracks" :key="index" :path="path" stroke-color="#d81e06" :stroke-opacity="0.7" :stroke-weight="5"></bm-polyline>

      <!-- 起点 -->
      <bm-marker :position="startPoint" :icon="startIcon" :offset="{ width: 0, height: -20 }"></bm-marker>
      <!-- 终点 -->
      <bm-marker :position="endPoint" :icon="finalIcon" :offset="{ width: 0, height: -20 }"></bm-marker>
    </baidu-map>
  </div>
</template>

<script>
import { BmlLushu } from 'vue-baidu-map'
// import { getOldMapOrderInfo } from '@/api/mapRoutes.js'

export default {
  name: 'RoutePlay',
  components: { BmlLushu },
  props: {
    vehicleMac: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      // 百度地图核心类
      BMap: null,
      // 控件偏移量
      controlOffset: { width: 20, height: 20 },
      // 地图中心点坐标
      center: { lng: 0, lat: 0 },
      // 地图层级
      mapZoom: 14,
      // 路线轨迹
      tracks: [],
      // 停留点数组
      stopPoints: [],
      // 起点
      startPoint: { lng: 0, lat: 0 },
      // 终点
      endPoint: { lng: 0, lat: 0 },
      // 路书轨迹回放当前播放路径 []
      currentTrack: [],
      // 当前回放工单的第几条路径计数（allTrack索引）
      trackCount: 0,
      // 路线回放动画播放控制
      playback: false,
      // 控制路书的显示
      lushuShow: false,
      // 路书关闭延时器
      lushuCloseTimer: null,
      // 路书图标
      lushuIcon: {
        url: require('@/assets/baidu_map/avatar.png'),
        size: { width: 50, height: 90 },
        opts: { imageSize: { width: 50, height: 50 } },
      },
      // 停留点图标
      // stopIcon: {
      //   url: require('@/assets/baidu_map/stop.png'),
      //   size: { width: 40, height: 40 },
      //   opts: { imageSize: { width: 40, height: 40 } },
      // },
      // 起点图标
      startIcon: {
        url: require('@/assets/baidu_map/start.png'),
        size: { width: 48, height: 48 },
        opts: { imageSize: { width: 48, height: 48 } },
      },
      // 终点图标
      finalIcon: {
        url: require('@/assets/baidu_map/end.png'),
        size: { width: 48, height: 48 },
        opts: { imageSize: { width: 48, height: 48 } },
      },
    }
  },
  // mounted() {
  //   getOldMapOrderInfo([this.patrolId]).then(res => {
  //     // console.log(res)
  //     // 设置停留点
  //     const stopArr = res.data[this.patrolId + '停留时间']
  //     if (stopArr.length > 0) {
  //       this.stopPoints = stopArr.map(item => {
  //         return {
  //           show: false,
  //           lng: item.longitude,
  //           lat: item.latitude,
  //           stayId: item.stayId,
  //           stayMinutes: item.stayMinutes,
  //         }
  //       })
  //     }
  //     // 设置路线
  //     let tracksTemp = []
  //     const routesObj = res.data[this.patrolId]
  //     let n = 0
  //     while (routesObj && routesObj[n]) {
  //       let line = routesObj[n++]
  //       if (line.length <= 1) continue
  //       let lineArr = []
  //       line.forEach(point => {
  //         lineArr.push({
  //           lng: point.longitude,
  //           lat: point.latitude,
  //         })
  //       })
  //       tracksTemp.push(lineArr)
  //     }
  //     this.tracks = tracksTemp
  //     // 如果路径确实有值执行后续操作（防止接口空值报错）
  //     if (tracksTemp.length > 0) {
  //       // 设置中心点
  //       let firstLine = tracksTemp[0]
  //       let lastLine = tracksTemp[tracksTemp.length - 1]
  //       // 设置起点、终点
  //       this.startPoint.lng = firstLine[0].lng
  //       this.startPoint.lat = firstLine[0].lat
  //       this.endPoint.lng = lastLine[lastLine.length - 1].lng
  //       this.endPoint.lat = lastLine[lastLine.length - 1].lat
  //       // 控制中心点和层级
  //       this.calculateCenterAndZoom(tracksTemp)
  //     }
  //   })
  // },
  beforeDestroy() {
    clearTimeout(this.lushuCloseTimer)
  },
  methods: {
    // 百度地图加载完成的回调
    mapHandler({ BMap, Map }) {
      // console.log('百度地图加载完成', BMap, Map);
      this.center.lng = 119.9927
      this.center.lat = 31.811863
      this.BMap = BMap
    },
    // 根据路线计算中心点和层级
    calculateCenterAndZoom(tracks) {
      // 最大最小经度和纬度
      let maxlng = 0 // 路线最大经度
      let minlng = 180 // 路线最小经度
      let maxlat = 0 // 路线最大纬度
      let minlat = 90 // 路线最小纬度
      tracks.forEach(track => {
        track.forEach(point => {
          if (point.lng > maxlng) maxlng = point.lng
          if (point.lng < minlng) minlng = point.lng
          if (point.lat > maxlat) maxlat = point.lat
          if (point.lat < minlat) minlat = point.lat
        })
      })
      // 控制层级
      const lngDiff = maxlng - minlng // 经度差
      const latDiff = maxlat - minlat // 纬度差
      const baseLngDiff = 0.004 // 经度差值基数
      const baseLatDiff = 0.002 // 纬度差值基数
      let n = 0
      while (lngDiff > baseLngDiff * Math.pow(2, n) || latDiff > baseLatDiff * Math.pow(2, n)) n++
      const resultZoom = 19 - n < 9 ? 9 : 19 - n
      this.mapZoom = resultZoom
      // 中心点
      this.center.lng = (maxlng + minlng) / 2
      this.center.lat = (maxlat + minlat) / 2
    },
    // 控件按钮点击事件-开始路书
    startLushu() {
      clearTimeout(this.lushuCloseTimer)
      this.lushuShow = false
      this.trackCount = 0
      this.currentTrack = this.tracks[this.trackCount].map(item => {
        return new this.BMap.Point(item.lng, item.lat)
      })
      setTimeout(() => {
        this.lushuShow = true
        this.playback = true
      }, 500)
    },
    // 路书-一段路线回放完毕、停止事件
    resetTrack() {
      this.trackCount++
      if (this.trackCount >= this.tracks.length) {
        this.playback = false
        this.lushuCloseTimer = setTimeout(() => {
          this.lushuShow = false
        }, 2000)
      } else {
        this.currentTrack = this.tracks[this.trackCount].map(item => {
          return new this.BMap.Point(item.lng, item.lat)
        })
      }
    },
    // 问题点信息窗体打开关闭事件
    infoWindowOpen(e, obj) {
      obj.show = true
    },
    infoWindowClose(e, obj) {
      obj.show = false
    },
  },
}
</script>

<style lang="scss" scoped>
.map-wrapper {
  width: 100%;
  height: 600px;
  .baidu-map {
    height: 100%;
    .play-lushu {
      box-shadow: 0 5px 10px 0 #999;
    }
  }
}
</style>

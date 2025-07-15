<template>
  <div class="big-view">
    <div class="top">
      <div class="title-left"></div>
      <div class="title">
        <span>无人机监控中心</span>
      </div>
      <div class="title-right">
        <screenfull id="screenfull" class="right-menu-item hover-effect" />
      </div>
    </div>
    <div class="card-video">
      <!-- <scroll-view style="height: 100%; width: 210px; z-index=1000;" direction="vertical" scrollY=true>
        test 2222
      </scroll-view> -->
      <CustomCard v-for="(uav, index) in uavVideoList" :key="index" :title="uav.title" class="cards-video-wrapper">
        <MonitorWindow :playerSrc="uav.videoUrl" :playerID="index" :title="uav.title"></MonitorWindow>
      </CustomCard>
    </div>
    <div class="card-container">
      <button class="nav-button left" @click="scrollLeft">◀</button>
      <div class="cards-wrapper" ref="cardsWrapper">
        <uav-card v-for="(uav, index) in uavList" :key="index" :image-url="uav.imageUrl" :ground-speed="uav.groundSpeed"
          :vehicle-soc="uav.vehicleSoc" :alt="uav.Alt" :status="uav.status" @card-click="handleCardClick" />
      </div>
      <button class="nav-button right" @click="scrollRight">▶</button>
    </div>
    <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center" :scroll-wheel-zoom="true"
      mapType="BMAP_SATELLITE_MAP" @ready="mapHandler" @mousemove="syncPolyline" @click="paintPolyline"
      @rightclick="newPolyline">
      <bm-control>
      </bm-control>
      <bm-polyline v-for="(path, index) in polyline.paths" :key="index" :path="path"></bm-polyline>
    </baidu-map>
    <div class="uav-status">
      <!-- <scroll-view style="height: 100%; width: 210px; z-index=1000;" direction="vertical" scrollY=true>
        test 2222
      </scroll-view> -->
      <CustomCard v-for="(uav, index) in uavVideo2List" :key="index" :title="uav.title" class="cards-video-wrapper">
        <MonitorWindow :playerSrc="uav.videoUrl" :playerID="index + 4"></MonitorWindow>
      </CustomCard>
    </div>
  </div>
</template>

<script>
import Screenfull from '@/components/Screenfull'
import UavCard from './components/UavCard.vue'
import MonitorWindow from './components/MonitorWindow' // 视频组件
import CustomCard from './components/customCard.vue'
export default {
  name: 'bigView',
  components: {
    Screenfull,
    UavCard,
    MonitorWindow,
    CustomCard,
  },
  data() {
    return {
      // 百度地图核心类
      BMap: null,
      // 控件偏移量
      controlOffset: { width: 20, height: 20 },
      // 地图中心点坐标
      center: { lng: 0, lat: 0 },
      startPoint: { lng: 119.9927, lat: 31.811863 },
      // 地图层级
      mapZoom: 15,
      polyline: {
        editing: false,
        paths: []
      },
      // WebSocket服务器地址
      webSocketServerUrl: 'ws://223.112.179.125:38080/',
      uavVideo2List: [
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台5"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台6"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台7"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台8"
        },
        /*  {
           videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
           title: "云台3"
         },
         {
           videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
           title: "云台4"
         }, */
      ],
      uavVideoList: [
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台1"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台2"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台3"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台4"
        },
        /*  {
           videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
           title: "云台3"
         },
         {
           videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
           title: "云台4"
         }, */
      ],
      uavList: [
        {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 5.6,
          vehicleSoc: 78,
          Alt: 120,
          status: 'normal'
        },
        {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 4.2,
          vehicleSoc: 65,
          Alt: 95,
          status: 'warning'
        },
        {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 3.8,
          vehicleSoc: 42,
          Alt: 80,
          status: 'error'
        },
        /* {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 5.6,
          vehicleSoc: 78,
          Alt: 120,
          status: 'normal'
        },
        {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 4.2,
          vehicleSoc: 65,
          Alt: 95,
          status: 'warning'
        },
        {
          imageUrl: require('@/assets/images/dashboard/uav.gif'),
          groundSpeed: 3.8,
          vehicleSoc: 42,
          Alt: 80,
          status: 'error'
        }, */
      ],
      currentIndex: 0,
      cardWidth: 300,
      socket: null,
      points: [],
      startPoint: { lon: 119.9927, lat: 31.811863 },
      endPoint: { lon: 120.9961, lat: 32.813883 },
      intervalId: null,  // 定时器
    }
  },
  created() {
    // this.initializeWebSocket()
  },
  beforeDestroy() {
    this.closeInterval()
    // 断开WebSocket连接
    this.disconnectWebSocket()
  },
  methods: {
    // 百度地图加载完成的回调
    mapHandler({ BMap, Map }) {
      // console.log('百度地图加载完成', BMap, Map);
      this.center.lng = 119.9927
      this.center.lat = 31.811863
      this.BMap = BMap
    },
    initializeWebSocket() {
      this.socket = new WebSocket(this.webSocketServerUrl);

      // 监听连接打开事件
      this.socket.onopen = function (event) {
        console.log('WebSocket连接成功');
        // reconnectAttempts = 0; // 重置重连尝试次数
        // startHeartbeat(); // 开始心跳检测
      };
    },
    // 发送消息到WebSocket服务器
    sendMessage(points) {
      // console.log('发送消息到WebSocket服务器', points)
      var i = 0
      if (i == points.length) {
        this.closeInterval()
        this.disconnectWebSocket()
      } else {
        this.intervalId = setInterval(() => {
          const vehicleInfo = {
            "fid": 101,
            "groundSpeed": 0.10824473202228546,
            "vehicleSoc": 100,
            "lng": points[i].lon,
            "lat": points[i].lat,
            "Alt": 10,
            "flyInAir": 0,
            "headAngle": 237.102051,
            "gpsStatu": 0,
            "satcount": 0,
            "mode": "Stabilize"
          }
          console.log('发送消息到WebSocket服务器', vehicleInfo)
          if (this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(vehicleInfo));
            i++
          } else {
            console.log('WebSocket连接未打开，消息暂存待发送');
            // 这里可以添加逻辑来暂存消息，待连接打开后发送
          }
        }, 500)
      };

    },
    toggle(name) {
      this[name].editing = !this[name].editing
      console.log(this[name].editing)
      if (!this[name].editing) {
        this.initializeWebSocket()
        this.calculatePoint(this.polyline.paths[0])
      }
    },
    syncPolyline(e) {
      if (!this.polyline.editing) {
        return
      }
      const { paths } = this.polyline
      if (!paths.length) {
        return
      }
      const path = paths[paths.length - 1]
      if (!path.length) {
        return
      }
      if (path.length === 1) {
        path.push(e.point)
      }
      this.$set(path, path.length - 1, e.point)
    },
    newPolyline(e) {
      if (!this.polyline.editing) {
        return
      }
      const { paths } = this.polyline
      if (!paths.length) {
        paths.push([])
      }
      const path = paths[paths.length - 1]
      path.pop()
      if (path.length) {
        paths.push([])
      }
    },
    paintPolyline(e) {
      if (!this.polyline.editing) {
        return
      }
      const { paths } = this.polyline
      !paths.length && paths.push([])
      paths[paths.length - 1].push(e.point)
    },
    calculatePoint(paths) {
      for (var i = 0; i < paths.length - 1; i++) {
        this.startPoint.lon = paths[i].lng
        this.startPoint.lat = paths[i].lat
        this.endPoint.lon = paths[i + 1].lng
        this.endPoint.lat = paths[i + 1].lat
        const speed = 100
        const reportInterval = 0.5
        const dx = this.endPoint.lon - this.startPoint.lon;
        const dy = this.endPoint.lat - this.startPoint.lat;
        const distance = Math.sqrt(dx * dx + dy * dy) * 111319;
        const totalTime = distance / speed;
        const numPoints = Math.ceil(totalTime / reportInterval) + 1;
        for (let i = 0; i < numPoints; i++) {
          const t = i / (numPoints - 1); // 插值比例
          const lon = this.startPoint.lon + t * dx;
          const lat = this.startPoint.lat + t * dy;
          this.points.push({ lon, lat });
        }
      }
      this.sendMessage(this.points)
    },
    closeInterval() {
      if (this.intervalId) {
        clearInterval(this.intervalId);
      }
    },
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close();
        this.socket = null;
        // clearHeartbeat();
      }
    },
    scrollLeft() {
      this.currentIndex = Math.max(0, this.currentIndex - 1)
      this.$refs.cardsWrapper.style.transform = `translateX(-${this.currentIndex * this.cardWidth}px)`
    },
    scrollRight() {
      this.currentIndex = Math.min(this.uavList.length - 1, this.currentIndex + 1)
      this.$refs.cardsWrapper.style.transform = `translateX(-${this.currentIndex * this.cardWidth}px)`
    },
    handleCardClick(uavId) {
      console.log('选中无人机:', uavId)
    }
  }
}
</script>

<style lang="scss" scoped>
.big-view {
  height: 100%;
  background: url('../../assets/images/dashboard/back.png');
  background-size: cover;
  background-position: center center;
  position: relative;
  overflow-y: hidden;


  .top {
    width: 100%;
    height: 10%;
    // padding-top: 1rem;
    padding-bottom: 3rem !important;
    padding-top: 0 !important;
    background: url('../../assets/images/dashboard/top-title.png') no-repeat;
    // background-size: 100% 100%;
    // /* 背景图垂直、水平均居中 */
    background-position: center center;
    // /* 让背景图基于容器大小伸缩 */
    background-size: cover;
    display: flex;
    justify-content: center;
    align-items: center;

    .title-left {
      flex: 1;
    }

    .title {
      margin-top: 12px;
      color: #45caff;
      font-size: 1.8rem;
      font-weight: 700;
      text-shadow: 0 0 2px #45caff;
      letter-spacing: 0.2rem;
      display: flex;
      justify-content: center;
      align-items: center;
      flex: 1;
    }

    .title-right {
      flex: 1;

      #screenfull {
        position: fixed;
        right: 10px;
      }
    }
  }

  .baidu-map {
    height: 100%;
    position: relative;
    top: 0px;
  }

  .play-lushu {
    box-shadow: 0 5px 10px 0 #999;
  }
}

.card-video {
  position: absolute;
  background: rgba(9, 37, 58, 0.8);
  top: 10%;
  left: 1px;
  height: 100%;
  width: 210px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  z-index: 1000;
}

.uav-status {
  position: absolute;
  background: rgba(9, 37, 58, 0.8);
  top: 10%;
  right: 1px;
  height: 100%;
  width: 210px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  z-index: 1000;
}

.card-container {
  position: absolute;
  bottom: 20px;
  left: 220px;
  right: 220px;
  height: 160px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;

  .nav-button {
    background: rgba(9, 37, 58, 0.8);
    border: 1px solid #0a4a6b;
    color: #45caff;
    padding: 10px 15px;
    border-radius: 4px;
    cursor: pointer;
    z-index: 1001;
  }

  .cards-wrapper {
    flex: 1;
    display: flex;
    justify-content: center;

    overflow: auto;
    scroll-behavior: smooth;
    padding: 0 10px;
  }
}
</style>

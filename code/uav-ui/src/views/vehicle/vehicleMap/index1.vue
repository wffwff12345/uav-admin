<template>
  <div class="map-wrapper">
    <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center" :scroll-wheel-zoom="true"
      mapType="BMAP_SATELLITE_MAP" @ready="mapHandler" @mousemove="syncPolyline" @click="paintPolyline"
      @rightclick="newPolyline">
      <bm-control style="display: flex; flex-direction: column; padding-left: 10px;margin-top: 10px;">
        <el-input style="width: 120px; margin-top: 10px;" v-model="fid" />
        <button style="height: 36px; margin-top: 10px;" @click="toggle('polyline')">{{ polyline.editing ? '停止绘制' : '开始绘制'
        }}</button>
      </bm-control>
      <bm-polyline v-for="(path, index) in polyline.paths" :key="index" :path="path"></bm-polyline>
    </baidu-map>
  </div>
</template>

<script>
export default {
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
      webSocketServerUrl: 'ws://10.1.2.45:28080/',
      socket: null,
      points: [],
      startPoint: { lon: 119.9927, lat: 31.811863 },
      endPoint: { lon: 120.9961, lat: 32.813883 },
      intervalId: null,  // 定时器
      fid: 101,
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
            "fid": this.fid,
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
      console.log(e.point)
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
    }
  }
}
</script>
<style lang="scss" scoped>
.map-wrapper {
  width: 100%;
  // height: 600px;
  height: 800px;

  .baidu-map {
    height: 100%;

    .play-lushu {
      box-shadow: 0 5px 10px 0 #999;
    }
  }
}
</style>

<template>
  <div class="page-container">
    <CustomPageDrawer from="left" :openFlag.sync="panelSwitch">
      <div class="panel-left">
        <div class="card">
          <CustomCard title="云台状态">
            <GimbalCard :vehicleInfo="getData"></GimbalCard>
          </CustomCard>
        </div>
        <div class="card">
          <CustomCard title="无人机视频">
            <MonitorWindow :playerSrc="videoSrc" playerID="bigVideo"></MonitorWindow>
          </CustomCard>
        </div>
      </div>
    </CustomPageDrawer>

    <CustomPageDrawer from="right" :openFlag.sync="panelSwitch">
      <div class="panel-right">
        <div class="card-full">
          <CustomCard title="无人机状态">
            <GeneralCard :vehicleInfo="getData"></GeneralCard>
          </CustomCard>
        </div>
      </div>
    </CustomPageDrawer>


    <div class="map-wrapper">
      <!-- 百度地图vue-baidu-map -->
      <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center" :scroll-wheel-zoom="true"
        mapType="BMAP_SATELLITE_MAP" @ready="mapHandler" @click="mapClick">
        <!-- 比例尺 -->
        <!-- <bm-scale anchor="BMAP_ANCHOR_TOP_RIGHT" :offset="controlOffset"></bm-scale> -->
        <!-- 行进轨迹 -->
        <bm-polyline :path="polylinePath" stroke-color="blue" :stroke-opacity="0.7" :stroke-weight="4" :editing="false"
          @lineupdate="updatePolylinePath"></bm-polyline>

        <!-- 自定义控件：播放轨迹 -->
        <bm-control anchor="BMAP_ANCHOR_TOP_LEFT" :offset="controlOffset">
          <!-- <el-row>
            <el-text style="color: white">无人机MAC:{{ getData.mac || '---' }}</el-text>
            <el-text style="color: white;margin-left: 20px;">无人机速度:{{ getData.vehicleSpeed || '---' }}KM/S</el-text>
            <el-text style="color: white;margin-left: 20px;">无人机电量:{{ getData.vehicleSoc || '---' }}</el-text>
          </el-row> -->
          <el-row>
            <el-button class="play-lushu" @click="clearData">清除轨迹</el-button>
          </el-row>
          <el-row>
            <el-button class="play-lushu" @click="clearPoint">清除点</el-button>
          </el-row>
        </bm-control>

        <!--<bm-control anchor="BMAP_ANCHOR_TOP_LEFT" :offset="controlOffset">
        <el-button class="play-lushu" @click="clearData">清除轨迹</el-button>
      </bm-control> -->

        <!-- <bm-control anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :offset="controlOffset">
          <el-text style="color: white;margin-right: 20px;">无人机经度:{{ getData.vehicleLong || '---' }}</el-text>
          <el-text style="color: white;margin-right: 20px;">无人机纬度:{{ getData.vehicleLat || '---' }}</el-text>
          <el-text style="color: white;">无人机海拔高度:{{ getData.vehicleAlt || '---' }}</el-text>
        </bm-control> -->

        <!-- 图表 -->
        <bm-marker :position="startPoint" :icon="lushuIcon" :offset="{ width: 0, height: 0 }"></bm-marker>

        <bm-marker :position="clickPoint" animation="BMAP_ANIMATION_BOUNCE">
          <bm-label :content="mapContent" :icon="clickIcon" :offset="{ width: -35, height: 30 }" />
        </bm-marker>
      </baidu-map>
    </div>
  </div>
</template>

<script>
import { BmlLushu } from 'vue-baidu-map'
import CustomPageDrawer from './components/customPageDrawer.vue'
import GimbalCard from './components/gimbalCard.vue'
import CustomCard from './components/customCard.vue'
import GeneralCard from './components/generalSituationCard.vue'
import MonitorWindow from './components/MonitorWindow' // 视频组件

export default {
  components: {
    CustomPageDrawer,
    CustomCard,
    GeneralCard,
    GimbalCard,
    MonitorWindow,
  },
  name: 'VehicleMap',
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
      polylinePath: [],
      // 路书图标
      lushuIcon: {
        url: require('@/assets/baidu_map/vehicle.gif'),
        size: { width: 150, height: 150 },
        opts: { imageSize: { width: 150, height: 150 } },
      },
      clickIcon: {
        url: require('@/assets/baidu_map/map-address.png'),
        size: { width: 150, height: 150 },
        opts: { imageSize: { width: 150, height: 150 } },
      },
      // 无人机列表
      vehicleList: [],
      vehicleSelected: null,
      // WebSocket服务器地址
      webSocketServerUrl: 'ws://223.112.179.125:38080/websocket/web/101',
      socket: null,
      getData: {
        mac: '',
        vehicleSpeed: '',
        vehicleSoc: '',
        vehicleLong: '',
        vehicleLat: '',
        vehicleAlt: '',
        faultInfo: '',
      },

      panelSwitch: true,
      // videoSrc: 'http://10.1.2.173:8889/mystream/czuft/forsun',
      videoSrc: 'http://10.1.2.173:90/hls/jslh_test.m3u8',
      // videoSrc: 'http://223.112.179.125:27002/hls/stream2.m3u8',
      bigVideo: 1,
      clickPoint: { lng: 119.9927, lat: 31.811863 },
      mapContent: '',

      x_PI: 3.14159265358979324 * 3000.0 / 180.0,
      PI: 3.1415926535897932384626,
      a: 6378245.0,
      ee: 0.00669342162296594323,

    }
  },
  created() {
    this.initializeWebSocket()
    this.polylinePath = []
    // listUavVehicleInfo().then(resp => {
    //   this.vehicleList = resp.rows
    //   this.vehicleSelected = resp.rows[0]
    // })
  },
  beforeDestroy() {
    // 断开WebSocket连接
    this.disconnectWebSocket()
  },
  mounted() {
    // 设置地图容器高度（因为外部高度是要计算calc的不确定高度）
    this.$el.style.height = this.$parent.$el.clientHeight + 'px'
  },
  methods: {
    // 百度地图加载完成的回调
    mapHandler({ BMap, Map }) {
      // console.log('百度地图加载完成', BMap, Map);
      this.center.lng = 119.9927
      this.center.lat = 31.811863
      this.BMap = BMap
    },
    updatePolylinePath(e) {
      this.polylinePath = e.target.getPath()
    },
    addPolylinePoint(newLng, newLat) {
      this.polylinePath.push({ lng: newLng, lat: newLat })
      this.startPoint.lng = newLng
      this.startPoint.lat = newLat
      // this.center.lng = newLng
      // this.center.lat = newLat
    },
    initializeWebSocket() {
      this.socket = new WebSocket(this.webSocketServerUrl);

      // 监听连接打开事件
      this.socket.onopen = function (event) {
        console.log('WebSocket连接成功');
        // reconnectAttempts = 0; // 重置重连尝试次数
        // startHeartbeat(); // 开始心跳检测
      };

      // 监听接收消息事件
      this.socket.onmessage = (event) => {
        const data = JSON.parse(event.data.split("#")[1]);
        this.getData.fid = data.fid
        this.getData.groundSpeed = data.groundSpeed.toFixed(6)
        this.getData.vehicleSoc = data.vehicleSoc.toFixed(6)
        // this.getData.vehicleLong = data.vehicleLong.toFixed(6)
        // this.getData.vehicleLat = data.vehicleLat.toFixed(6)
        this.getData.Alt = data.Alt.toFixed(6)

        // wgs84先转为火星坐标系
        const gcj02 = this.wgs84togcj02(data.lng, data.lat)
        console.log(gcj02)

        // 火星坐标系转百度坐标系
        const result = this.gcj02tobd09(gcj02[0], gcj02[1])
        this.getData.lng = result[0]
        this.getData.lat = result[1]

        this.addPolylinePoint(this.getData.lng, this.getData.lat)
      };
    },
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close();
        this.socket = null;
        // clearHeartbeat();
      }
    },
    clearData() {
      this.getData = {
        mac: '',
        vehicleSpeed: '',
        vehicleSoc: '',
        vehicleLong: '',
        vehicleLat: '',
        vehicleAlt: '',
        faultInfo: '',
      }
      this.polylinePath = []
    },
    clearPoint() {
      this.clickPoint.lat = 0
      this.clickPoint.lng = 0
    },
    mapClick(event) {
      this.clickPoint.lat = event.Dg.lat
      this.clickPoint.lng = event.Dg.lng
      this.mapContent = '点击坐标：' + event.Dg.lng + ',' + event.Dg.lat
    },
    wgs84togcj02(lng, lat) {
      if (this.outOfChina(lng, lat)) {
        return [lng, lat]
      }
      else {
        var dlat = this.transformlat(lng - 105.0, lat - 35.0)
        var dlng = this.transformlng(lng - 105.0, lat - 35.0)
        var radlat = lat / 180.0 * this.PI
        var magic = Math.sin(radlat)
        magic = 1 - this.ee * magic * magic
        var sqrtmagic = Math.sqrt(magic)
        dlat = (dlat * 180.0) / ((this.a * (1 - this.ee)) / (magic * sqrtmagic) * this.PI)
        dlng = (dlng * 180.0) / (this.a / sqrtmagic * Math.cos(radlat) * this.PI)
        const mglat = lat + dlat
        const mglng = lng + dlng
        return [mglng, mglat]
      }
    },
    outOfChina(lng, lat) {
      return (lng < 72.004 || lng > 137.8347) || ((lat < 0.8293 || lat > 55.8271) || false)
    },
    transformlat(lng, lat) {
      var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng))
      ret += (20.0 * Math.sin(6.0 * lng * this.PI) + 20.0 * Math.sin(2.0 * lng * this.PI)) * 2.0 / 3.0
      ret += (20.0 * Math.sin(lat * this.PI) + 40.0 * Math.sin(lat / 3.0 * this.PI)) * 2.0 / 3.0
      ret += (160.0 * Math.sin(lat / 12.0 * this.PI) + 320 * Math.sin(lat * this.PI / 30.0)) * 2.0 / 3.0
      return ret
    },
    transformlng(lng, lat) {
      var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng))
      ret += (20.0 * Math.sin(6.0 * lng * this.PI) + 20.0 * Math.sin(2.0 * lng * this.PI)) * 2.0 / 3.0
      ret += (20.0 * Math.sin(lng * this.PI) + 40.0 * Math.sin(lng / 3.0 * this.PI)) * 2.0 / 3.0
      ret += (150.0 * Math.sin(lng / 12.0 * this.PI) + 300.0 * Math.sin(lng / 30.0 * this.PI)) * 2.0 / 3.0
      return ret
    },
    gcj02tobd09(lng, lat) {
      var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * this.x_PI)
      console.log(lng * lng)
      var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * this.x_PI)
      var bd_lng = z * Math.cos(theta) + 0.0065
      var bd_lat = z * Math.sin(theta) + 0.006
      return [bd_lng, bd_lat]
    }
  }
}
</script>
<style lang="scss" scoped>
.map-wrapper {
  height: 100%;

  .baidu-map {
    height: 100%;

    .play-lushu {
      margin-top: 10px;
      box-shadow: 0 5px 10px 0 #999;
    }
  }
}
</style>

<style lang="scss" scoped>
.page-container {

  .panel-left {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: rgba(3, 30, 69, 0.8);

    .card {
      height: 50%;
      min-height: 0;
    }


    .card1 {
      height: 17%;
      min-height: 0;
    }

    .card2 {
      height: 49%;
      min-height: 0;
    }

    .card3 {
      height: 34%;
      min-height: 0;
    }
  }

  .panel-right {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: rgba(3, 30, 69, 0.8);


    .card {
      height: 50%;
      min-height: 0;
    }

    .card-full {
      height: 100%;
      min-height: 0;
    }

    .card-long {
      height: 50%;
      min-height: 0;
    }
  }
}

.video-box {
  height: 100%;

  .vjs-custom-player {
    width: 100%;
    height: 100%;
    font-size: 1rem;
  }
}
</style>

<template>
  <div class="data-body">
    <div class="top-title">
      <ul style="height: 30px; margin-bottom: 0px;">
        <li class="l_left total_chose_fr " :class="{ 'nav_active': activeTab === 'monitor' }"
          @click="setActiveTab('monitor')">实时监测</li>
        <li class="l_left total_chose_pl" :class="{ 'nav_active': activeTab === 'hangar' }"
          @click="setActiveTab('hangar')">机库</li>
        <li class="l_left total_chose_pl" :class="{ 'nav_active': activeTab === 'task' }" @click="setActiveTab('task')">
          飞行策略规划</li>
      </ul>
      <div class="title">
        <span>无人机监控中心</span>
      </div>
      <div class="title-right">
        <!-- <screenfull id="screenfull" class="right-menu-item hover-effect" /> -->
        <p class="boxDay">{{ dateWeek }}</p>
        <p class="boxDate">{{ dateYear }}</p>
        <p class="boxTime">{{ dateDay }}</p>
      </div>
    </div>
    <div class="body" v-if="body">
      <div class="body-left">
        <div class="maps">
          <img class="land_level" src="@/assets/img/landLevel.png" alt="">
          <img class="wifi_gif" src="@/assets/img/wifi.gif" alt="">
          <img class="sun_pic" src="@/assets/img/sun.png" alt="">
          <img class="wrj_pic" src="@/assets/img/wrj.png" alt="">
          <img class="wind_gif" src="@/assets/img/wind_shape.gif" alt="">
        </div>
        <div class="card-video">
          <CustomCard v-for="(uav, index) in uavVideoList" :key="index" :title="uav.title" class="cards-video-wrapper">
            <MonitorWindow :playerSrc="uav.videoUrl" :playerID="index" :title="uav.title"></MonitorWindow>
          </CustomCard>
        </div>
      </div>
      <div class="body-center">
        <div class="map-wrapper">
          <!-- 百度地图vue-baidu-map -->
          <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center"
            :scroll-wheel-zoom="true" mapType="BMAP_SATELLITE_MAP" @ready="mapHandler" @click="mapClick">
            <bm-polyline v-for="(line, index) in dynamicPolylines" :key="line.id" :path="line.path"
              :stroke-color="line.color" :stroke-opacity="0.7" :stroke-weight="4"></bm-polyline>
            <!-- 自定义控件：播放轨迹 -->
            <bm-control anchor="BMAP_ANCHOR_TOP_LEFT" :offset="controlOffset">
              <el-row>
                <el-button class="play-lushu" @click="clearData">清除轨迹</el-button>
              </el-row>
            </bm-control>
            <template v-for="(line, lineIndex) in dynamicPolylines">
              <bm-marker v-if="line.path.length > 0" :key="`${line.id}_last`" :position="line.path[line.path.length - 1]"
                :icon="lushuIcon" :offset="{ width: 0, height: 0 }">
                <!-- 可选：显示高度信息 -->
                <bm-label v-if="showAltitude && line.path.length > 0" :position="line.path[line.path.length - 1]"
                  :content="`${line.path[line.path.length - 1].alt}m`" :offset="{ width: 60, height: 10 }"
                  :labelStyle="{ color: '#fff', background: 'rgba(0,0,0,0.5)', padding: '2px 5px' }" />
              </bm-marker>
            </template>
          </baidu-map>
        </div>
      </div>
      <div class="body-right">
        <div class="cards-wrapper" ref="cardsWrapper">
          <UavCard v-for="(uav, index) in uavList" :key="index" :image-url="uav.imageUrl" :ground-speed="uav.groundSpeed"
            :vehicle-soc="uav.vehicleSoc" :alt="uav.Alt" :status="uav.status" />
        </div>
      </div>
    </div>
    <div class="body" v-if="task">
      <div class="task-body-left">

        <span>飞行策略规划</span>
        <hr style="border: 0.5px solid #398dc2; margin: 20px 0;">

        <!-- <el-button style="width: 100px; height: 36px;" size="medium" type="primary">新增</el-button> -->

        <div class="select-uav">
          <span class="chose_title">请选择无人机：</span>
          <!-- 添加 popper-class 自定义类名 -->
          <el-select v-model="TaskUavName" placeholder="请选择" popper-class="custom-uav-dropdown">
            <el-option v-for="item in uavTaskOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>

        <el-input placeholder="请输入策略名称" v-model="taskSearchName" class="input-with-select" style=" margin: 20px 0;">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>

        <UavTask/>

        <span class="top-left border-span"></span>
        <span class="top-right border-span"></span>
        <span class="bottom-left border-span"></span>
        <span class="bottom-right border-span"></span>
      </div>

      <div class="task-body-center">
        <div class="map-wrapper">
          <!-- 百度地图vue-baidu-map -->
          <baidu-map class="baidu-map" :zoom="mapZoom" :min-zoom="9" :max-zoom="19" :center="center"
            :scroll-wheel-zoom="true" mapType="BMAP_SATELLITE_MAP" @ready="mapHandler" @click="mapClick">
            <bm-polyline :path="taskPolylines" stroke-color="blue" :stroke-opacity="0.7" :stroke-weight="4"
              :editing="false"></bm-polyline>
            <!-- 遍历路径点添加序号标记 -->
            <template v-for="(point, index) in taskPolylines">
              <bm-marker :position="{ lng: point.lng, lat: point.lat }" :offset="{ width: 0, height: 2 }">
                <bm-label :content="String(index + 1)" :labelStyle="{
                  color: '#fff',
                  fontSize: '14px',
                  backgroundColor: '#ff5722',
                  borderRadius: '12px',
                  padding: '2px 8px',
                  boxShadow: '0 0 4px rgba(0,0,0,0.3)'
                }" :offset="{ width: -4.5, height: -5 }" />
              </bm-marker>
            </template>
          </baidu-map>
        </div>
      </div>
      <div class="task-body-right">
        <div style="height: 90%;">
          <span>修改飞行策略</span>
          <hr style="border: 0.5px solid #398dc2; margin: 20px 0;">

          <span>策略名称</span>
          <el-input style="margin-top: 8px;" autosize placeholder="策略名称" v-model="taskName" />

          <span style="display: inline-block; width: 100%;margin-bottom: 8px;margin-top: 16px;">飞行完成后的动作</span>
          <el-select style="width: 100%;" v-model="terminalTaskType" placeholder="请选择">
            <el-option v-for="item in terminalTaskOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>

          <el-button type="primary" style="width: 50%; height: 36px;margin-bottom: 8px; margin-top: 32px;">航点</el-button>
          <el-table :data="taskItems" height="224" style="overflow-y: auto;" class="unifiedtwo-table"
            header-row-class-name="unifiedtwo-header-row" header-cell-class-name="unifiedtwo-header-cell"
            row-class-name="unifiedtwo-tbody-row" cell-class-name="unifiedtwo-tbody-cell">
            <el-table-column prop="name" label="航点">
            </el-table-column>
            <el-table-column prop="alt" label="高度(m)">
            </el-table-column>
            <el-table-column prop="hoverTime" label="悬停时间(s)">
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button style="color:#36e0e7" size="mini" type="text"
                  @click="handleUpdate(scope.$index)">修改</el-button>
                <el-button style="color:#36e0e7" size="mini" type="text"
                  @click="handleDelete(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div style="text-align: center; height: 5%; width: 100%; position: absolute; bottom: 16px; margin-top: 8px;">
          <el-button style="width: 100px; height: 36px;" size="medium" type="primary">取消</el-button>
          <el-button style="width: 100px; height: 36px;" size="medium" type="primary">保存</el-button>
        </div>

        <span class="top-left border-span"></span>
        <span class="top-right border-span"></span>
        <span class="bottom-left border-span"></span>
        <span class="bottom-right border-span"></span>
      </div>

      <el-dialog title="任务项" :visible.sync="taskItemOpen" width="500px" append-to-body>
        <el-form ref="taskItemform" class="taskItemForm" :model="taskItemform" label-width="100px">
          <el-form-item class="margin-bottom: 20px" label="名称：" prop="name">
            <el-input v-model="taskItemform.name" readonly />
          </el-form-item>
          <el-form-item class="margin-bottom: 20px" label="经度：" prop="lng">
            <el-input v-model="taskItemform.lng" readonly />
          </el-form-item>
          <el-form-item class="margin-bottom: 20px" label="纬度：" prop="lat">
            <el-input v-model="taskItemform.lat" readonly />
          </el-form-item>
          <el-form-item class="margin-bottom: 20px" label="高度：" prop="alt">
            <el-input v-model="taskItemform.alt" />
          </el-form-item>
          <el-form-item class="margin-bottom: 20px" label="悬停时间：" prop="hoverTime">
            <el-input class="width: 50%;" v-model="taskItemform.hoverTime" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button style="width: 100px; height: 36px;" type="primary" @click="taskItemSubmitForm">确 定</el-button>
          <el-button style="width: 100px; height: 36px;" @click="taskItemCancel">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import Screenfull from '@/components/Screenfull'
import MonitorWindow from './components/MonitorWindow' // 视频组件
import CustomCard from './components/customCard.vue'
import UavCard from './components/uavCard.vue'
import UavTask from './components/uavTask.vue'
import dayjs from "dayjs";

export default {
  components: {
    UavCard,
    Screenfull,
    MonitorWindow,
    CustomCard,
    UavTask
  },
  data() {
    return {
      activeTab: 'monitor',
      body: true,
      task: false,
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
        //url: require('@/assets/images/plane.png'),
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
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台5"
        },
        {
          videoUrl: "http://223.112.179.125:23081/hls/stream2.m3u8",
          title: "云台6"
        },
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
      vehicleSelected: null,
      // WebSocket服务器地址
      webSocketServerUrl: 'ws://223.112.179.125:38080/websocket/web',
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
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"],
      timer: null,
      dynamicPolylines: [], // 格式：[{id: 'drone1', path: [], color: '#ff0000'}, ...]
      taskPolylines: [],
      colorPool: ['#ff0000', '#00ff00', '#0000ff', '#ffff00', '#ff00ff'], // 颜色池
      showAltitude: true,
      uavTaskOptions: [{
        value: '1',
        label: '无人机1'
      }, {
        value: '2',
        label: '无人机2'
      }, {
        value: '3',
        label: '无人机3'
      }],
      terminalTaskOptions: [{
        value: '1',
        label: '返航'
      }, {
        value: '2',
        label: '原点降落'
      }, {
        value: '3',
        label: '悬停'
      }],
      terminalTaskType: '',
      filterMissions: [{ lat: 1, lon: 2 }, { lat: 1, lon: 2 }],
      taskItems: [
        /* { name: "1", speed: 1, alt: 5 },
        { name: "2", speed: 1, alt: 5 },
        { name: "3", speed: 1, alt: 5 },
        { name: "4", speed: 1, alt: 5 },
        { name: "5", speed: 1, alt: 5 },
        { name: "6", speed: 1, alt: 5 }, */
      ],
      taskName: "",
      taskSearchName: "",
      TaskUavName: "",

      taskItemOpen: false,
      taskItemform: {},
      currentItemIndex: 0,
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
    this.disconnectWebSocket();
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  mounted() {
    // 设置地图容器高度（因为外部高度是要计算calc的不确定高度）
    this.$el.style.height = this.$parent.$el.clientHeight + 'px';
    this.timer = setInterval(() => {
      const date = dayjs(new Date());
      this.dateDay = date.format("HH:mm:ss");
      this.dateYear = date.format("YYYY-MM-DD");
      this.dateWeek = date.format(this.weekday[date.day()]);
    }, 1000);
  },
  methods: {
    setActiveTab(tab) {
      this.activeTab = tab;
      if (tab === "monitor") {
        this.body = true;
        this.task = false
      } else if (tab === "task") {
        this.taskPolylines = [];
        this.body = false;
        this.task = true;
      }
    },
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
      //this.center.lng = newLng
      //this.center.lat = newLat
    },
    initializeWebSocket() {
      this.socket = new WebSocket(this.webSocketServerUrl);

      // 监听连接打开事件
      this.socket.onopen = function (event) {
        console.log('WebSocket连接成功');
      };
      // 监听接收消息事件
      this.socket.onmessage = (event) => {
        const data = JSON.parse(event.data.split("#")[1]);
        // wgs84先转为火星坐标系
        const gcj02 = this.wgs84togcj02(data.lng, data.lat)

        // 火星坐标系转百度坐标系
        const bdPoint = this.gcj02tobd09(gcj02[0], gcj02[1])
        // 2. 更新对应轨迹
        this.updateTrackLine({
          fid: data.fid,
          point: bdPoint,
          alt: data.Alt
        });
      };
    },
    // 更新轨迹线
    updateTrackLine(trackData) {
      // 查找或创建轨迹对象
      let track = this.dynamicPolylines.find(t => t.id === trackData.fid);

      if (!track) {
        track = this.createNewTrack(trackData.fid);
        this.dynamicPolylines.push(track);
      }

      // 更新轨迹数据
      track.path.push({
        lng: trackData.point[0],
        lat: trackData.point[1],
        alt: trackData.alt
      });

      // 性能优化：保持最大点数
      /* if (track.path.length > this.maxPoints) {
        track.path.pop();
      } */
    },

    // 创建新轨迹
    createNewTrack(deviceId) {
      return {
        id: deviceId,
        path: [],
        color: this.colorPool[Math.floor(Math.random() * this.colorPool.length)],
        altHistory: [] // 可选：高度历史数据
      };
    },

    // 清除所有轨迹
    clearAllTracks() {
      this.dynamicPolylines = [];
    },

    // 清除单个轨迹
    clearTrack(deviceId) {
      this.dynamicPolylines = this.dynamicPolylines.filter(
        t => t.id !== deviceId
      );
    },
    disconnectWebSocket() {
      if (this.socket) {
        this.socket.close();
        this.socket = null;
        // clearHeartbeat();
      }
    },
    clearData() {
      /* this.getData = {
        mac: '',
        vehicleSpeed: '',
        vehicleSoc: '',
        vehicleLong: '',
        vehicleLat: '',
        vehicleAlt: '',
        faultInfo: '',
      }
      this.polylinePath = [] */
    },
    clearPoint() {
      this.clickPoint.lat = 0
      this.clickPoint.lng = 0
    },
    mapClick(e) {
      this.taskPolylines.push(e.point);
      this.taskItems.push({
        name: `任务${this.taskItems.length + 1}`,
        lat: e.point.lat,
        lng: e.point.lng,
        alt: 5,
        hoverTime: 1
      });
    },
    handleUpdate(index) {
      this.currentItemIndex = index;
      this.taskItemOpen = true;
      this.taskItemform = JSON.parse(JSON.stringify(this.taskItems[index]));
    },


    handleDelete(index) {
      this.taskPolylines.splice(index, 1);
      this.taskItems.splice(index, 1);
      for (let i = index; i <= this.taskItems.length - 1; i++) {
        this.taskItems[i].name = `任务${i + 1}`;
      }
    },
    taskItemSubmitForm() {
      console.log({ ...this.taskItemform });
      this.taskItems.splice(this.currentItemIndex, 1, { ...this.taskItemform });
      this.taskItemform = {};
      this.taskItemOpen = false;
    },
    taskItemCancel() {
      this.taskItemform = {};
      this.taskItemOpen = false;
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
    },
  }
}
</script>

<style lang="less" scoped>
*,
ul,
li,
p,
div,
span {
  margin: 0;
  padding: 0
}

body {
  font-family: "Microsoft Himalaya"
}

ul {
  list-style: none
}

.data-body {
  width: 100%;
  height: 100%;
  background-image: url('../../assets/images/dashboard/back.png');
  background-size: 100% 100%;
  position: relative;
  overflow-y: hidden;
}

.top-title {
  width: 100%;
  height: 10%;
  // padding-top: 1rem;
  padding-top: 0 !important;
  background: url('../../assets/images/dashboard/top-title.png') no-repeat;
  // background-size: 100% 100%;
  // /* 背景图垂直、水平均居中 */
  background-position: center center;
  // /* 让背景图基于容器大小伸缩 */
  background-size: cover;

  position: relative;

  /* display: flex;
  flex-direction: row;
  justify-content: flex-start;
  .l_left {
    float: left;
  } */
}

.top-title ul li {
  font-size: 1em;
  color: #fff;
  opacity: 0.8;
  padding-top: 8px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 5px;
  cursor: pointer;
  text-shadow: 0 6px 8px #00225b;
  float: left;
}

.top-title ul .l_left {
  margin-left: 3%
}

.top-title ul .r_right {
  margin-right: 3%
}

.top-title ul li:hover {
  opacity: 1
}

.title {
  width: 30%;
  height: 50%;
  color: #fff;
  opacity: 0.8;
  padding-top: 8px;
  padding-left: 10px;
  padding-right: 10px;
  padding-bottom: 5px;
  cursor: pointer;
  font-size: 1.8rem;
  font-weight: 700;
  text-shadow: 0 0 2px #45caff;
  letter-spacing: 0.2rem;
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
  float: left;
  position: absolute;
  left: 50%;
  top: 30%;
  transform: translate(-50%, -50%);
}

.title-right {
  position: absolute;
  top: 20%;
  right: 20px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  color: #fff;

  .boxDay {
    font-size: 16px;
  }

  .boxTime {
    font-size: 16px;
  }

  .boxDate {
    margin: 0 15px;
    font-size: 16px;
  }
}

.nav_active {
  /*box-shadow: -10px 0px 15px rgba(2,8,23,0.54) inset,*/
  /*0px -10px 15px rgba(2,8,23,0.54) inset,*/
  /*10px 0px 15px rgba(2,8,23,0.54) inset,*/
  /*0px 10px 15px rgba(2,8,23,0.54) inset;*/
  border-bottom: 4px solid #4b8df8;
  box-sizing: border-box;
}

.body {
  width: 100%;
  height: 90%;
  position: relative;
}

.body-left {
  /* border-radius: 10px;
  border: 1px #0174f5 solid; */
  padding: 1px;
  box-sizing: border-box;
  width: 22%;
  height: 100%;
  margin-left: 0.3%;
  float: left;

  .maps {
    position: relative;
    width: 100%;
    height: 30%;
    border-radius: 10px;
    border: 1px #0174f5 solid;
    padding: 1px;
    box-sizing: border-box;
    /* background: pink; */
    /* background-image: url(../img/landLevel.png); */
    /* background-size: 95% 100%; */
    /* background-repeat: no-repeat; */
    /* background-position: center; */
  }

  .maps .land_level {
    width: 95%;
    height: 100%;
    margin-left: 2.5%;
  }

  .maps .wifi_gif {
    position: absolute;
    right: 27%;
    top: -2.5%;
    width: 5%;
    height: 10%;
  }

  .maps .sun_pic {
    position: absolute;
    top: 2%;
    left: 18%;
    width: 10%;
    height: 15%;

    animation: mymove 3s infinite;
    -webkit-animation: mymove 3s infinite;
    /*Safari and Chrome*/
    animation-direction: alternate;
    /*轮流反向播放动画。*/
    animation-timing-function: ease-in-out;
    /*动画的速度曲线*/
    /* Safari 和 Chrome */
    -webkit-animation: mymove 3s infinite;
    -webkit-animation-direction: alternate;
    /*轮流反向播放动画。*/
    -webkit-animation-timing-function: ease-in-out;
  }

  .maps .wrj_pic {
    position: absolute;
    width: 8%;
    height: 8%;
    left: 30%;
    animation: myfirst 5s infinite ease-in-out;
    -moz-animation: myfirst 5s infinite;
    /* Firefox */
    -webkit-animation: myfirst 5s infinite;
    /* Safari 和 Chrome */
    -o-animation: myfirst 5s infinite;
    /* Opera */
    /* animation: btn-load-loop 1s linear infinite; */
  }

  @keyframes myfirst {
    0% {
      left: 30%;
      top: 0px;
    }

    25% {
      left: 30%;
      top: 30%;
    }

    50% {
      left: 40%;
      top: 70%;
    }

    75% {
      left: 50%;
      top: 80%;
    }

    100% {
      left: 50%;
      top: 20%;
    }
  }

  @-moz-keyframes myfirst {

    /* Firefox */
    0% {
      left: 30%;
      top: 0px;
    }

    25% {
      left: 30%;
      top: 30%;
    }

    50% {
      left: 40%;
      top: 70%;
    }

    75% {
      left: 50%;
      top: 80%;
    }

    100% {
      left: 50%;
      top: 20%;
    }
  }

  @-webkit-keyframes myfirst

  /* Safari 和 Chrome */
    {
    0% {
      left: 30%;
      top: 0px;
    }

    25% {
      left: 30%;
      top: 30%;
    }

    50% {
      left: 40%;
      top: 70%;
    }

    75% {
      left: 50%;
      top: 80%;
    }

    100% {
      left: 50%;
      top: 20%;
    }
  }

  @-o-keyframes myfirst

  /* Opera */
    {
    0% {
      left: 30%;
      top: 0px;
    }

    25% {
      left: 30%;
      top: 30%;
    }

    50% {
      left: 40%;
      top: 70%;
    }

    75% {
      left: 50%;
      top: 80%;
    }

    100% {
      left: 50%;
      top: 20%;
    }
  }

  .maps .wind_gif {
    position: absolute;
    top: 25%;
    left: 5%;
    width: 10%;
    height: 19%;
  }

  @keyframes mymove {
    0% {
      transform: scale(1);
      /*开始为原始大小*/
    }

    25% {
      transform: scale(1.1);
      /*放大1.1倍*/
    }

    50% {
      transform: scale(1.05);
    }

    75% {
      transform: scale(1);
    }

  }

  @-webkit-keyframes mymove

  /*Safari and Chrome*/
    {
    0% {
      transform: scale(1);
      /*开始为原始大小*/
    }

    25% {
      transform: scale(1.1);
      /*放大1.1倍*/
    }

    50% {
      transform: scale(1.05);
    }

    75% {
      transform: scale(1);
    }
  }

  .card-video {
    margin-top: 1.5%;
    height: 70%;
    overflow-y: auto;
    overflow-x: hidden;
    border-radius: 10px;
    border: 1px #0174f5 solid;
    padding: 1px;
    box-sizing: border-box;
    /* 隐藏所有滚动条但保留功能 */
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* IE/Edge */

    /* Webkit浏览器隐藏滚动条 (Chrome, Safari等) */
    &::-webkit-scrollbar {
      display: none;
      /* 完全隐藏滚动条 */
    }
  }
}

.task-body-left {
  position: absolute;
  left: 20px;
  top: 40px;
  background-color: #192f3c;
  opacity: 0.8;
  box-sizing: border-box;
  padding: 16px;
  width: 28%;
  height: 88%;
  z-index: 1000;
  color: #fff;
  border: 1px solid #54dcf2;

  span.border-span {
    display: block;
    position: absolute;
    width: 35px;
    height: 35px;
  }

  span.top-left {
    top: -4px;
    left: -4px;
    border-top: 3px solid #54dcf2;
    border-left: 3px solid #54dcf2;
  }

  span.top-right {
    top: -4px;
    right: -4px;
    border-top: 3px solid #54dcf2;
    border-right: 3px solid #54dcf2;
  }

  span.bottom-left {
    bottom: -4px;
    left: -4px;
    border-bottom: 3px solid #54dcf2;
    border-left: 3px solid #54dcf2;
  }

  span.bottom-right {
    bottom: -4px;
    right: -4px;
    border-bottom: 3px solid #54dcf2;
    border-right: 3px solid #54dcf2;
  }

  .task-card {
    background:url("../../assets/images/time.png"); 
    background-size: 100% 100%; 
    background-repeat: no-repeat;
    background-position: top center; 
    width:100%; 
    height:9.3%; 
    margin-left: 6%;
  }

}

.body-center {
  border-radius: 10px;
  border: 1px #0174f5 solid;
  padding: 1px;
  box-sizing: border-box;
  width: 55.1%;
  height: 100%;
  margin-left: 0.3%;
  float: left;

  .map-wrapper {
    height: 100%;
    border-radius: 10px;
    border: 1px #0174f5 solid;
    padding: 1px;
    box-sizing: border-box;

    .baidu-map {
      height: 100%;

      .play-lushu {
        height: 28px;
        width: 120px;
        margin-top: 10px;
        box-shadow: 0 5px 10px 0 #999;
      }
    }
  }
}

.task-body-center {
  /* border-radius: 10px;
  border: 1px #0174f5 solid; */
  padding: 1px;
  box-sizing: border-box;
  width: 100%;
  height: 100%;

  .map-wrapper {
    height: 100%;
    border-radius: 10px;
    border: 1px #0174f5 solid;
    padding: 1px;
    box-sizing: border-box;

    .baidu-map {
      height: 100%;

      .play-lushu {
        height: 28px;
        width: 120px;
        margin-top: 10px;
        box-shadow: 0 5px 10px 0 #999;
      }
    }
  }
}

.body-right {
  border-radius: 10px;
  border: 1px #0174f5 solid;
  padding: 1px;
  box-sizing: border-box;
  width: 22%;
  height: 100%;
  margin-left: 0.3%;
  float: right;

  .cards-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    /* 隐藏所有滚动条但保留功能 */
    scrollbar-width: none;
    /* Firefox */
    -ms-overflow-style: none;
    /* IE/Edge */

    /* Webkit浏览器隐藏滚动条 (Chrome, Safari等) */
    &::-webkit-scrollbar {
      display: none;
      /* 完全隐藏滚动条 */
    }
  }
}

.task-body-right {
  position: absolute;
  opacity: 0.8;
  right: 20px;
  top: 40px;
  background-color: #192f3c;
  padding: 16px;
  box-sizing: border-box;
  width: 28%;
  height: 88%;
  z-index: 1000;
  color: #fff;
  border: 1px solid #54dcf2;

  span.border-span {
    display: block;
    position: absolute;
    width: 35px;
    height: 35px;
  }

  span.top-left {
    top: -4px;
    left: -4px;
    border-top: 3px solid #54dcf2;
    border-left: 3px solid #54dcf2;
  }

  span.top-right {
    top: -4px;
    right: -4px;
    border-top: 3px solid #54dcf2;
    border-right: 3px solid #54dcf2;
  }

  span.bottom-left {
    bottom: -4px;
    left: -4px;
    border-bottom: 3px solid #54dcf2;
    border-left: 3px solid #54dcf2;
  }

  span.bottom-right {
    bottom: -4px;
    right: -4px;
    border-bottom: 3px solid #54dcf2;
    border-right: 3px solid #54dcf2;
  }
}

// 第二种样式>>>
@row-height2: 2.3rem; // 行高
@cell-padding2: 0; // 单元格内边距
@cell-div-padding2: 0; // 单元格里内容内边距
@t-bgc2: transparent; // 表格整体背景颜色
@th-bgc2: transparent; // 表头背景色
@table-font-size2: 1rem; // 表格整体字体大小

::v-deep .unifiedtwo-table-area {
  height: 100%;
  // border: solid 0.1rem #45caff;
  border-bottom-style: solid;
  border-color: #45caff;
  border-width: 0.1rem;

  .unifiedtwo-table {
    border-bottom: none;
    font-size: @table-font-size2; // 表格字体大小
    background-color: @t-bgc2;

    &::before {
      background-color: @t-bgc2; // 消除el-table底部的白线
    }

    .unifiedtwo-header-row {
      height: @row-height2;
      background-color: @t-bgc2;
      box-shadow: inset 0 0 5px 3px #45caff;

      .unifiedtwo-header-cell {
        padding: @cell-padding2;
        border-bottom: none;
        font-size: @table-font-size2; // 表头字体大小
        color: #45caff;
        background-color: @th-bgc2;

        div {
          padding: @cell-div-padding2;
        }
      }

      // 表头和滚动条交叉区域
      .gutter {
        background-color: @th-bgc2;
      }
    }

    .unifiedtwo-tbody-row {
      height: @row-height2;
      background-color: @t-bgc2;

      &:hover {
        .unifiedtwo-tbody-cell {
          background-color: #03426e;
        }
      }

      .unifiedtwo-tbody-cell {
        padding: @cell-padding2;
        border-bottom: none;
        color: #ffffff;
        cursor: default;
        letter-spacing: 0;

        div {
          padding: @cell-div-padding2;
        }
      }
    }

    .unifiedtwo-table-btn {
      // 表格内el-button,el-link样式类，作为el-button,el-link的class手动添加
      font-size: @table-font-size2;
      color: #36e0e7;
    }
  }
}

::v-deep .el-table__empty-block {
  background-color: #00416f;
}

::v-deep .el-table th {
  background-color: #00416f;
  color: white;
  // border: solid #1AAADB;
  // background-color: rgba(255,255,255,0);
}


//去除table的每一条数据的下边框
::v-deep .el-table td {
  border-bottom: none;
  background-color: #012e4d;
  color: white;
}

/* 修改悬停行背景色 */
::v-deep .el-table--enable-row-hover .el-table__body tr:hover>td {
  background-color: #02416d;
}

//去除表格的最下面一行的边框
// .tableStyle::before {
//   width: 0;
// }

//设置表的外边框
.el-table {
  border: 1px solid #1AAADB;
}

// ----------修改elementui表格的默认样式-----------
::v-deep .el-table__body-wrapper {
  &::-webkit-scrollbar {
    // 整个滚动条
    width: 0; // 纵向滚动条的宽度
    background: rgba(213, 215, 220, 0.3);
    border: none;
  }

  &::-webkit-scrollbar-track {
    // 滚动条轨道
    border: none;
  }
}

// --------------------隐藏table gutter列和内容区右侧的空白 start
::v-deep .el-table th.gutter {
  display: none;
  width: 0
}

::v-deep .el-table colgroup col[name='gutter'] {
  display: none;
  width: 0;
}

::v-deep .el-table__body {
  width: 100% !important;
}

::v-deep .el-table__body-wrapper {
  background-color: #03426e;
}

/* 深度作用样式 */
.select-uav {
  /deep/ .custom-uav-dropdown {
    .el-select-dropdown__list {
      background-color: #1AAADB !important;

      .el-select-dropdown__item {
        color: white;

        &:hover {
          background-color: rgba(255, 255, 255, 0.2);
        }
      }
    }
  }
}

// 选择下拉框样式修改
::v-deep .el-input,
::v-deep .el-input__inner {
  border-radius: 10px;
  //border: 1px solid green;
  border-radius: 0px;
  border-color: #1AAADB;
  text-align: center;
  color: #fff;
  background-color: transparent;
}

/* 
// 选择下拉框样式修改
::v-deep .el-input,
::v-deep .el-input__inner {
  border-radius: 10px;
  //border: 1px solid green;
  border-radius: 0px;
  border-color: #1AAADB;
  text-align: center;
  background-color: rgba(0, 0, 0, 0);
}

//el-input聚焦的时候 外层的border会有一个样式
::v-deep .el-select .el-input.is-focus .el-input__inner {
  color: #fff;
  //border:0px;
  border: 1px solid #1AAADB;
}

//修改的是el-input上下的小图标的颜色
::v-deep .el-select .el-input .el-select__caret {
  color: #fff;
}
 */
//修改总体选项的样式 最外层
/* ::v-deep .el-select-dropdown {
  //rgba(87,133,87,0.8)
  background: red;
  //margin: 0px;
  border: 0px;
  //border-radius: 0px;
  //left: 0px !important;
  text-align: center;
} */

::v-deep .unifiedtwo-table .el-table__empty-block {
  background-color: #03426e;
}

::v-deep .el-form-item {
  margin-bottom: 20px;
}

.taskItemForm {
  color: black;

  ::v-deep .el-input__inner {
    color: black;
  }
}

/* 在 scoped 样式中使用 */
/* ::v-deep .el-select-dropdown__list {
  background-color: red !important;
} */

::v-deep .el-select-dropdown__item {
  text-align: center !important;
  /* 可选：添加其他样式确保生效 */
  display: block !important;
}
</style>

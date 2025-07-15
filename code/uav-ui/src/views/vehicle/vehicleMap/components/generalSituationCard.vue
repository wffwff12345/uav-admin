<template>
  <div class="content-area">

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">无人机状态</div>
          <div class="line-col-2">
            <i class="el-icon-position" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-purple">
              <span v-if="vehicleInfo.vehicleSpeed">在线</span>
              <span v-else>离线</span>
            </div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">站点海拔高度</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-purple">{{ vehicleInfo.vehicleAlt || '0' }}m</div>
          </div>
        </div>
      </div>

      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">经度</div>
          <div class="line-col-3 font-color-purple">{{
            vehicleInfo.vehicleLong || '---' }}</div>
        </div>
        <div style="display: flex; flex:1; flex-direction: column; ">
          <div class="line-col-1">纬度</div>
          <div class="line-col-3 font-color-purple">{{
            vehicleInfo.vehicleLat || '---' }}</div>
        </div>
      </div>
    </div>

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">飞行模式</div>
          <div class="line-col-2">
            <i class="el-icon-position" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">
              <!-- <span v-if="vehicleInfo.vehicleSpeed">在线</span>
              <span v-else>离线</span> -->
            </div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">当前飞行高度</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div>
          </div>
        </div>
      </div>

      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">飞行距离</div>
          <div class="line-col-2">
            <i class="el-icon-position" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">距站点距离</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div>
          </div>
        </div>
      </div>

      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">水平速度 m/s</div>
          <img src="@/assets/images/001.png" alt="" style="width: 125px; height: 125px;">
        </div>
        <div class="line-col">
          <div class="line-col-1">垂直速度 m/s</div>
          <img src="@/assets/images/001.png" alt="" style="width: 125px; height: 125px;">
        </div>
      </div>
    </div>

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">GPS信号</div>
          <div class="line-col-2">
            <i class="el-icon-position" style="margin-left: 10px;"></i>
            <!-- <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div> -->
            <div class="line-col-3 font-color-blue" style="margin-top: -5px;">强</div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">遥控信号</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <!-- <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div> -->
            <div class="line-col-3 font-color-blue" style="margin-top: -5px;">强</div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">图传信号</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <!-- <div class="line-col-3 font-color-blue">{{ vehicleInfo.vehicleAlt || '0' }}m</div> -->
            <div class="line-col-3 font-color-blue" style="margin-top: -5px;">强</div>
          </div>
        </div>
      </div>

      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">无人机电量</div>
          <div class="line-col-2">
            <i class="el-icon-position" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">
              <span v-if="vehicleInfo.vehicleSpeed">在线</span>
              <span v-else>离线</span>
            </div>
          </div>
        </div>
        <div class="line-col">
          <div class="line-col-1">电池循环充电次数</div>
          <div class="line-col-2">
            <i class="el-icon-guide" style="margin-left: 10px;"></i>
            <div class="line-col-3 font-color-blue">0</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: '',
  props: {
    vehicleInfo: {
      type: Object,
    }
  },
  data() {
    return {}
  },
  created() { },
  mounted() { },
  beforeDestroy() { },
  watch: {},
}
</script>

<style lang="scss" scoped>
.content-area {
  display: flex;
  flex-direction: column;
  color: white;
}

.area {
  display: flex;
  flex-direction: column;
}

.area-line {
  display: flex;
  flex-direction: row;
  margin: 5px 0;
}

.line-col {
  display: flex;
  flex: 1;
  flex-direction: column;
}

.line-col-1 {
  flex: 1;
  margin-left: 10px;
}

.line-col-2 {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  margin-top: 5px;
}

.line-col-3 {
  flex: 1;
  margin-left: 15px;
  align-items: center;
  justify-content: center;
  margin-top: 5px;
}

.font-color-purple {
  color: rgb(135, 132, 221);
}

.font-color-blue {
  color: rgb(68, 185, 211);
}

.cu-divider {
  margin: 6px 0 !important;
}
</style>

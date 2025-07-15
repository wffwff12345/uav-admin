<template>
  <div class="content-area">
    <div class="content">
      <div class="container">
        <div style="display: flex;">
          <div style="flex-direction: columns; font-size: 20px;">
            <div v-if="vehicleInfo.vehicleSpeed">无人机状态:在线</div>
            <div v-else>无人机状态:<span>离线</span></div>
            <div style="margin-top: 5px;">无人机MAC:{{ vehicleInfo.mac || '---' }}</div>
            <div style="margin-top: 5px;">无人机速度:{{ vehicleInfo.vehicleSpeed || '---' }}KM/S</div>
            <div style="margin-top: 5px;">无人机电量:{{ vehicleInfo.vehicleSoc || '---' }}</div>
            <div style="margin-top: 5px;">无人机经度:{{ vehicleInfo.vehicleLong || '---' }}</div>
            <div style="margin-top: 5px;">无人机纬度:{{ vehicleInfo.vehicleLat || '---' }}</div>
            <div style="margin-top: 5px;">无人机高度:{{ vehicleInfo.vehicleAlt || '---' }}</div>
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
  height: 100%;
  display: flex;
  // align-items: center;
  justify-content: begin;
}

.content {
  // margin-top: 30px;
  // text-align: center;
}

.container {
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: center;
  color: #5bcdf6;
  font-size: 1.8rem;
  font-weight: 700;
  letter-spacing: 0.2rem;
  // margin-top: 30px;
}

.div1,
.div2,
.div3 {
  display: flex;
  // align-items: center;
  // justify-content: center;
  // background: url("@/assets/img/secure/secure-day-background.png") no-repeat center/100% 100%;
  color: #ffffff;
  padding: 10px;
  // font-size: 30px;
  font-size: 40px;
  font-weight: bold;
  margin: 2px;
}
</style>

<template>
  <div class="content-area">

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">镜头选择</div>
          <div class="line-col-2">
            <div class="listCss" v-for="(item, index) in listArr" :key="index"
              :class="{ 'activeCss': activeVar == index }" @click="activeFun(item, index)">
              {{ item }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">变焦</div>
          <div class="line-col-3">
            <div style="flex:1;"><el-button icon="el-icon-minus" size="mini" @click="decreaseFocus"></el-button></div>
            <div style="flex:4;padding: 0 10px;"><el-slider v-model="focus" :max=56 :min=1 :marks="{ '1': '1', '56': '56' }"></el-slider></div>
            <div style="flex:1; margin-left: 10px"><el-button icon="el-icon-plus" size="mini" @click="increaseFocus"></el-button></div>
          </div>
        </div>
      </div>
    </div>

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <div class="line-col-1">云台俯仰角度</div>
          <div class="line-col-3">
            <div style="flex:1;"><el-button icon="el-icon-minus" size="mini" @click="decreaseAngle"></el-button></div>
            <div style="flex:4;padding: 0 10px;"><el-slider v-model="angle" :max=30 :min=-90 :marks="{ '-90': '-90°', '0': '0°', '30': '30°' }"></el-slider></div>
            <div style="flex:1; margin-left: 10px"><el-button icon="el-icon-plus" size="mini" @click="increaseAngle"></el-button></div>
          </div>
        </div>
      </div>
    </div>

    <el-divider class="cu-divider" />

    <div class="area">
      <div class="area-line">
        <div class="line-col">
          <el-button type="primary" style="margin: 0 10px;" @click="resetFocus">复位</el-button>
        </div>
        <div class="line-col">
          <el-button type="primary" style="margin: 0 10px;" @click="resetAngle">垂直向下</el-button>
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
    return {
      // 默认为0(如不需要默认可设为null)，点击时将索引赋值给active，从而实现点击选中效果
      activeVar: 0,
      //  循环列表
      listArr: ["变焦", "广角", "红外"],
      focus: 1,
      angle: 0,

    }
  },
  methods: {
    activeFun(item, index) {
      // item 为被选中的元素体
      this.activeVar = index
    },
    resetFocus() {
      this.focus = 1
    },
    increaseFocus() {
      if (this.focus == 56) {
        return
      }
      this.focus = this.focus + 1
    },
    decreaseFocus() {
      if(this.focus == 1) {
        return
      }
      this.focus = this.focus - 1
    },
    resetAngle() {
      this.angle = 0
    },
    increaseAngle() {
      if(this.angle == 30) {
        return
      }
      this.angle = this.angle + 1
    },
    decreaseAngle() {
      if(this.angle == -90) {
        return
      }
      this.angle = this.angle - 1
    },
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
  display: flex;
  flex-direction: row;
  width: 100%;
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

// 默认样式
.listCss {
  cursor: pointer;
  width: 75px;
  height: 35px;
  text-align: center;
  line-height: 35px;
  border: 1px solid #ccc;
  float: left;
  background: rgba(255, 255, 255, 0.4);
}

// 选中时的样式 (继承上方默认样式)
.activeCss {
  background: rgb(29, 155, 239);
}
</style>

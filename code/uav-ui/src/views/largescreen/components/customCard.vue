<template>
  <!-- 模块单元背景板 -->
  <div class="card-area">
    <!--  <div class="header">
      <div class="header-cu">
        <p class="cn-title">{{ title }}</p>
        <i v-if="showIcon" class="el-icon-date" style="padding-right: 10px;"></i>
      </div>
      <div style="border: 0.1rem solid rgba(255,255,255, 0.4); margin-top: 3px;"></div>
    </div> -->
    <span>{{ title }}</span>
    <div class="body">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CustomCard',
  props: {
    // 中文标题
    title: {
      type: String,
      default: '',
    },
    showIcon: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style lang="scss" scoped>
.card-area {
  height: 180px;
  color: #fff;
  text-align: center;
  padding-bottom: 0px;
  //background-color: @card-bgc;

  .header {
    height: 2.5rem;
    letter-spacing: 0.2rem;
    color: #fff;
    font-weight: 700;
    background-color: rgb(3, 30, 69);
    // background-image: url('../../assets/images/layout/custom-card-header.png');
    background-repeat: no-repeat;

    p {
      margin: 0;
      padding-left: 10px;
      line-height: 1.2;

      &.cn-title {
        font-size: 1.7rem;
      }

      &.en-title {
        font-size: 1rem;
      }
    }
  }

  .body {
    margin-top: 0.5rem;
    height: calc(100% - 3.2rem);

    overflow-y: auto;
  }

  .header-cu {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }
}
</style>

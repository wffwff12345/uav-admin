<template>
  <div class="uav-task-card">
    <div class="card-header">
      <span class="status-indicator" :class="statusClass">header</span>
    </div>
    <div class="card-body">
      <div class="data-row">
        <label>策略名称：</label>
        <span>{{ taskName }}</span>
        <hr style="border: 0.5px solid #398dc2;">
      </div>
      <div class="data-row">
        <label>执行时间：</label>
        <span>{{ executeTime }}</span>
        <hr style="border: 0.5px solid #398dc2;">
      </div>
      <div class="data-row">
        <label>任务项数量：</label>
        <span>{{ taskNumber }}</span>
        <hr style="border: 0.2px solid #398dc2;">
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    taskName: {
      type: String,
      default: '策略1'
    },
    executeTime: {
      type: String,
      default: '2025-10-2 10:20:10'
    },
    taskNumber: {
      type: Number,
      default: 3
    },
    status: {
      type: String,
      default: 'normal'
    }
  },
}
</script>

<style lang="scss" scoped>
.uav-task-card {
  background: url("../../../assets/images/time.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: top center;
  width: 100%;
  height: 25%;
  padding-left: 10%;
  padding-top: 2%;
  padding-bottom: 2%;
  padding-right: 2px;
  font-size: 12px;
  text-align: left;

  .data-row {
    margin-top: 12px;
  }
}
</style>
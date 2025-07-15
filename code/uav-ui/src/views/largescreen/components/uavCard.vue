<template>
  <div class="uav-card" @click="handleClick">
    <div class="card-header">
      <img :src="imageUrl" class="uav-thumbnail" />
      <span class="status-indicator" :class="statusClass"></span>
    </div>
    <div class="card-body">
      <div class="data-row">
        <label>飞行速度：</label>
        <span>{{ groundSpeed }} m/s</span>
      </div>
      <div class="data-row">
        <label>剩余电量：</label>
        <span>{{ vehicleSoc }}%</span>
      </div>
      <div class="data-row">
        <label>飞行高度：</label>
        <span>{{ Alt }} 米</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    imageUrl: {
      type: String,
      default: require('@/assets/images/dashboard/uav.gif')
    },
    groundSpeed: Number,
    vehicleSoc: Number,
    Alt: Number,
    status: {
      type: String,
      default: 'normal'
    }
  },
  computed: {
    statusClass() {
      return {
        'status-normal': this.status === 'normal',
        'status-warning': this.status === 'warning',
        'status-error': this.status === 'error'
      }
    }
  },
  methods: {
    handleClick() {
      this.$emit('card-click', this.$vnode.key)
    }
  }
}
</script>

<style lang="scss" scoped>
.uav-card {
  width: 100%;
  background: rgba(9, 37, 58, 0.8);
  border-radius: 10px;
  border: 1px #0174f5 solid;
  padding: 12px;
  margin-right: 15px;
  transition: transform 0.3s;
  cursor: pointer;
  display: flex;
  justify-content: space-evenly;
  align-items: center;

  &:hover {
    transform: translateY(-3px);
  }

  .card-header {
    position: relative;

    .uav-thumbnail {
      width: 120px;
      height: 120px;
      object-fit: cover;
      border-radius: 2px;
    }

    .status-indicator {
      position: absolute;
      right: 10px;
      top: 10px;
      width: 10px;
      height: 10px;
      border-radius: 50%;

      &.status-normal {
        background: #00ff00;
      }

      &.status-warning {
        background: #ffd700;
      }

      &.status-error {
        background: #ff0000;
      }
    }
  }

  .card-body {
    margin-left: 10%;
    padding: 10px 0;
    color: #45caff;
    display: flex;
    flex-direction: column;

    .data-row {
      display: flex;
      justify-content: space-between;
      margin: 8px 0;
      font-size: 14px;

      label {
        color: #7fbedf;
      }
    }
  }
}</style>
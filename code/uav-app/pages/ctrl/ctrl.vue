<template>
	<view>
		<view class="radius-10 sub-bg-color2 text-white flex a-center j-center m-10" @click="onBack"
			style="width: 100rpx;height: 50rpx;position: fixed;">
			返 回
		</view>
		<view class="text-white m-tb-5" style="position: fixed;left: 40%; top: 0;width: 350rpx;height: 50rpx;">
			{{"x:"+info.x +" | y:"+info.y+ " | deg:" +info.angle}}
		</view>

		<view class="radius-10 m-10" style="position: fixed;right: 0;border: 4rpx solid white;">
			<map name="smap" style="width: 190rpx;height: 100rpx;margin:0 0 -8rpx 0;" :latitude="latitude"
				:longitude="longitude" :markers="markers">
			</map>
		</view>

		<rocker style="position: fixed;bottom: 0;right: 0;"></rocker>

		<iframe id="inlineFrameExample" title="" :width="`${w}px`" :height="`${h}px`"
			src="http://10.1.2.173:8888/mystream/czuft" style="position:absolute;z-index: -1;">
		</iframe>
	</view>
</template>

<script>
	import rocker from '@/components/rocker/rocker.vue';
	export default {
		components: { rocker },
		data() {
			return {
				w: getApp().globalData.screen.width,
				h: getApp().globalData.screen.height,
				latitude: 31.82,
				longitude: 120.00,
				markers: [{
					id: 1,
					latitude: null,
					longitude: null,
					iconPath: "/static/imgs/drone-sm.png",
					anchor: { x: .5, y: .5 }
				}],
				info: {
					x: 0,
					y: 0,
					speed: 0,
					angle: 0
				}

			}
		},
		onReady() {
			uni.$on("rocker-move", (res) => {
				this.info = res
			})
		},
		onLoad() {
			this.markers[0].latitude = this.latitude
			this.markers[0].longitude = this.longitude
			setInterval(() => {
				this.markers[0].iconPath = this.markers[0].iconPath ? "" : "/static/imgs/drone-sm.png"
				this.markers = [...this.markers]
			}, 700)
		},
		methods: {
			onBack() {
				uni.navigateBack()
			},
		}
	}
</script>

<style>

</style>
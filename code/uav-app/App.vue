<script>
	export default {
		globalData: {
			screen: { width: null, height: null }
		},
		onLaunch: function() {
			// 获取屏幕高度
			uni.getSystemInfo({
				success: (res) => {
					this.globalData.screen.width = res.screenWidth
					this.globalData.screen.height = res.screenHeight
				}
			});
			plus.navigator.setFullscreen(true);
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
		@import url("static/css/common.css");
</style>
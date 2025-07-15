<template>
	<view>
		<canvas canvas-id="rocker" :disable-scroll="true" :style="`width: ${width}px; height: ${height}px;`"
			@touchstart="onTouchStart" @touchmove="onTouchMove" @touchend="onTouchEnd" @touchcancel="onTouchEnd">
		</canvas>
	</view>
</template>

<script>
	export default {
		name: "rocker",
		data() {
			return {
				ctx: null,
				coord: { x: 0, y: 0 },
				paint: false,
				width: 240,
				height: 240,
				radius: 80,
				// 摇杆半径
				rocker_r: 30,
				x_orig: null,
				y_orig: null,
				// x_delta: null,
				// y_delta: null,
				info: {
					x: 0,
					y: 0,
					angle: 0
				}
			};
		},
		mounted() {
			this.x_orig = this.width / 2
			this.y_orig = this.height / 2
			this.background()
			this.joystick(this.x_orig, this.y_orig)
			this.ctx.draw()
		},
		methods: {
			// 绘制背景
			background() {
				this.ctx = uni.createCanvasContext("rocker", this);
				this.ctx.beginPath();
				this.ctx.arc(this.x_orig, this.y_orig, this.radius, 0, Math.PI * 2);
				this.ctx.setFillStyle("rgba(255,255,255,0.3)")
				this.ctx.fill()
			},
			// 绘制手柄
			joystick(x, y) {
				this.ctx.beginPath();
				this.ctx.arc(x, y, this.rocker_r, 0, Math.PI * 2);
				this.ctx.fillStyle = '#00adb5';
				this.ctx.fill();
				this.ctx.strokeStyle = 'rgba(0, 20, 20, 0.1)';
				this.ctx.lineWidth = 8;
				this.ctx.stroke();
			},
			onTouchStart(e) {
				this.coord = { x: e.touches[0].x, y: e.touches[0].y }
				this.paint = true
				if (this.inCircle()) {
					this.drawAll();
				}
			},
			onTouchMove(e) {
				//console.log("onTouchMove.........");
				this.coord = { x: e.touches[0].x, y: e.touches[0].y }
				this.paint = true
				this.drawAll()
			},
			onTouchEnd() {
				//console.log("onTouchCancel..........");
				this.coord = { x: this.width / 2, y: this.height / 2 }
				this.drawAll()
				this.paint = false
			},
			inCircle() {
				var current_radius = Math.sqrt(Math.pow(this.coord.x - this.x_orig, 2) +
					Math.pow(this.y_orig - this.coord.y, 2));
				return this.radius >= current_radius
			},
			drawAll() {
				if (this.paint) {
					// 清除画布
					this.ctx.clearRect(0, 0, this.width, this.height);
					// 绘制背景
					this.background();

					let angle, angle_in_degrees, x, y;
					// 计算角度
					angle = Math.atan2((this.coord.y - this.y_orig), (this.coord.x - this.x_orig));
					// console.log(angle);
					if (Math.sign(angle) == -1) {
						angle_in_degrees = Math.round(-angle * 180 / Math.PI);
					} else {
						angle_in_degrees = Math.round(360 - angle * 180 / Math.PI);
					}

					if (this.inCircle()) {
						x = this.coord.x
						y = this.coord.y
					} else {
						// 计算圆周上的坐标
						// this.joystick(this.radius * Math.cos(angle) + this.x_orig,
						// 	this.radius * Math.sin(angle) + this.y_orig);
						x = this.radius * Math.cos(angle) + this.x_orig
						y = this.radius * Math.sin(angle) + this.y_orig
					}
					this.joystick(x, y)
					this.info = {
						x: Math.round(x - this.x_orig),
						y: Math.round(this.y_orig - y),
						speed: 0,
						angle: angle_in_degrees
					}
					uni.$emit("rocker-move", this.info)
					this.ctx.draw()
				}
			}
		},
	}
</script>

<style>

</style>
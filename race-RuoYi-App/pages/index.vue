<template>
  <!-- 页面全局配置 -->
  <view class="pageconfig" :style="{'padding-top':StatusBar + 'px'}">
    <!--vuetop-->
    <!--通知栏0的布局样式-->
    <view class="notice52   ">
      <uni-notice-bar color="#E62227" show-icon scrollable
                      text="uni-app 版正式发布，开发一次，同时发布iOS、Android、H5、微信小程序、支付宝小程序、百度小程序、头条小程序等7大平台。"/>
    </view>
    <view style="position:relative;" class="banner293   ">
      <swiper :current="swiperCurrent293" autoplay="true" @change="swiperChange293"
              style="height:300rpx;">
        <swiper-item class="u-swiper-item" v-for="(item, index293) in label293List" :key="index293">
          <view class="u-list-image-wrap">
            <image class="u-swiper-image" :src="item.image"></image>
          </view>
        </swiper-item>
      </swiper>
      <view class="u-swiper-indicator flex justify-center" :style="{width:  '100%',bottom:  '0px',padding: '24rpx'}">
        <block>
          <view class="u-indicator-item-number">{{ swiperCurrent293 + 1 }}/{{ label293List.length }}</view>
        </block>
      </view>
    </view>
    <view class=" tubiao783   cuIcon-like  text-" style="font-size: 46rpx;"></view>
    <!--弹窗容器3的布局样式-->
    <view class=" commoncss333   ">
      <view class="cu-modal  " :class="mymodelshow333">
        <view class="cu-dialog basis-lg  ">
          <view class="cu-bar bg-white justify-end">
            <view class="content">标题</view>
            <view class="action" @tap="hideModal333">
              <text class="cuIcon-close text-lg"></text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!--vuebottom-->

  </view>
</template>

<script>

/**    author:Mr.yang    **/
/**    qq:987095529   **/
/**    wx:wx987095529   **/
import Vue from 'vue'
import {
  mapState
} from 'vuex'

export default {
  data() {
    return {
      StatusBar: 0,
      swiperCurrent293: 0,
      /**single**/mymodelshow333: '',
      /**single**/

      label52List: [{"name": "一键生成代码"}, {"name": "一键复制代码"}, {"name": "可视化uniapp"}, {"name": "自动拖拽布局"}],
      /**vuejs**/
      label293List: [{
        "apiimg": "",
        "myname": "",
        "myurl": "",
        "evtform": [],
        "apidata": "",
        "myremark": "",
        "event": "",
        "title": "昨夜星辰昨夜风，画楼西畔桂堂东",
        "image": "https://shitangsys.cntdh.net//uploads/store/comment/20230304/27c20ac3f9566d77517f3365fe08682a.jpg",
        "pic": "https://shitangsys.cntdh.net//uploads/store/comment/20230304/27c20ac3f9566d77517f3365fe08682a.jpg"
      }, {
        "apiimg": "",
        "myname": "",
        "myurl": "",
        "evtform": [],
        "apidata": "",
        "myremark": "",
        "event": "",
        "title": "身无彩凤双飞翼，心有灵犀一点通",
        "image": "https://shitangsys.cntdh.net//uploads/store/comment/20230304/4ea7e37b68ed1b14230f7f2b5d343124.jpg",
        "pic": "https://shitangsys.cntdh.net//uploads/store/comment/20230304/4ea7e37b68ed1b14230f7f2b5d343124.jpg"
      }],
      /**vuejs**/


    };
  },
  onLoad() {
    this.getsysinfo();


  },
  methods: {


    //获取顶部状态栏的高度
    getsysinfo() {
      let that = this;
      uni.getSystemInfo({
        success: function (e) {
          // #ifndef MP
          that.StatusBar = e.statusBarHeight;
          if (e.platform == 'android') {
            that.StatusBar = e.statusBarHeight + 50;
          } else {
            that.StatusBar = e.statusBarHeight + 45;
          }
          ;
          // #endif

          // #ifdef MP-WEIXIN
          that.StatusBar = e.statusBarHeight;

          // #endif

          // #ifdef H5
          that.StatusBar = 0;

          // #endif

          // #ifdef MP-ALIPAY
          Vue.prototype.StatusBar = e.statusBarHeight;
          that.StatusBar = e.statusBarHeight + e.titleBarHeight;
          // #endif

        }
      })

    },


    /**
     *          $vuetag = "";
     *          $vuecss = "";
     *          $vuejs = "";
     *
     */

    swiperChange293(e) {
      this.swiperCurrent293 = e.detail.current;
    },
    hideModal333(e) {
      this.mymodelshow333 = "";
    },
    //获取数据

  }
}
</script>

<style lang="less">


/**
 *
 此处必须引用colorui样式,并放入根目录，css样式可以这样写
	@import "colorui/main.css";
	@import "colorui/icon.css";

 如需要引用colorui到全局，
 需要在App.vue 引入
 	@import "colorui/main.css";
	@import "colorui/icon.css";
 *
 */


/**本页全局配置的代码**/
.pageconfig {
  width: 100%;
  overflow: hidden;
  overflow-y: scroll;
}


/**pagedomcss**/


.notice52 {
  width: 100%;
  background-color: #ffffff;
}

/* 常用轮播图 css*/
.u-swiper-image {
  width: 100%;
  will-change: transform;
  height: 100%;
  /* #ifndef APP-NVUE */
  display: block;
  /* #endif */
  /* #ifdef H5 */
  pointer-events: none;
  /* #endif */
}

.u-swiper-indicator {
  padding: 0 24 rpx;
  position: absolute;
  z-index: 1;
}

.u-indicator-item-rect {
  width: 26 rpx;
  height: 8 rpx;
  margin: 0 6 rpx;
  transition: all 0.5s;
  background-color: rgba(0, 0, 0, 0.3);
}

.u-indicator-item-rect-active {
  background-color: rgba(255, 255, 255, 0.8);
}

.u-indicator-item-dot {
  width: 14 rpx;
  height: 14 rpx;
  margin: 0 6 rpx;
  border-radius: 20 rpx;
  transition: all 0.5s;
  background-color: rgba(0, 0, 0, 0.3);
}

.u-indicator-item-dot-active {
  background-color: rgba(255, 255, 255, 0.8);
}

.u-indicator-item-round {
  width: 14 rpx;
  height: 14 rpx;
  margin: 0 6 rpx;
  border-radius: 20 rpx;
  transition: all 0.5s;
  background-color: rgba(0, 0, 0, 0.3);
}

.u-indicator-item-round-active {
  width: 34 rpx;
  background-color: rgba(255, 255, 255, 0.8);
}

.u-indicator-item-number {
  padding: 6 rpx 16 rpx;
  line-height: 1;
  background-color: rgba(0, 0, 0, 0.3);
  border-radius: 100 rpx;
  font-size: 26 rpx;
  color: rgba(255, 255, 255, 0.8);
}

.u-list-scale {
  transform-origin: center center;
}

.u-list-image-wrap {
  width: 100%;
  height: 100%;
  flex: 1;
  transition: all 0.5s;
  overflow: hidden;
  box-sizing: content-box;
  position: relative;
}

.u-swiper-title {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.3);
  bottom: 0;
  left: 0;
  width: 100%;
  font-size: 28 rpx;
  padding: 12 rpx 24 rpx;
  color: rgba(255, 255, 255, 0.9);
}

.u-swiper-item {
  overflow: hidden;
  align-items: center;
}

.banner293 {
  width: 100%;
}

.commoncss333 {
  width: 100%;
}


</style>

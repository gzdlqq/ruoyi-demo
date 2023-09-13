<template>

    <!-- 页面全局配置 -->
    <view class="pageconfig" :style="{'padding-top':StatusBar + 'px'}">
        <!--vuetop-->
        <view class="flex187 padding-xs response animation-fade flex  justify-center  " style="flex-direction: row;align-content: center;align-items: stretch;">
            <view class="flex28188 padding-xs  flex  justify-center  " style="flex-direction: column;align-content: center;align-items: stretch;height:200rpx;min-height:unset;">
                <view class="picture929   ">
                    <image mode="widthFix" class=" " style="width:100%;" :src="picsrc929"></image>
                </view>
            </view>
            <view class="flex79578 padding-xs  flex flex-wrap justify-start  " style="flex-direction: row;">
                <view class="picture20520   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc20520"></image>
                </view>
                <view class="picture70739   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc70739"></image>
                </view>
                <view class="picture24724   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc24724"></image>
                </view>
                <view class="picture4004   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc4004"></image>
                </view>
            </view>
        </view>
        <!--通知栏1的布局样式-->
        <view class="notice395   ">
            <uni-notice-bar color="#E62227" show-icon scrollable text="uni-app 版正式发布，开发一次，同时发布iOS、Android、H5、微信小程序、支付宝小程序、百度小程序、头条小程序等7大平台。" />
        </view>
        <view class="flex765 padding-xs response  flex flex-wrap justify-between  " style="flex-direction: row;">
            <view class="picture655   ">
                <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc655"></image>
            </view>
            <view class="flex578   flex flex-wrap justify-start  " style="flex-direction: row;">
                <view class="picture135   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc135"></image>
                </view>
                <view class="picture28   ">
                    <image mode="widthFix" class=" radius" style="width:100%;" :src="picsrc28"></image>
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
                picsrc929: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                picsrc20520: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                picsrc70739: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                picsrc24724: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                picsrc4004: 'https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg',
                picsrc655: 'https://shitangsys.cntdh.net//uploads/store/comment/20230304/7eb53d6a38cefa81d910d5c350ff8410.jpg',
                picsrc135: 'https://shitangsys.cntdh.net//uploads/store/comment/20230304/b3cfd5fabd1eaa644a741bb3030f145c.jpg',
                picsrc28: 'https://shitangsys.cntdh.net//uploads/store/comment/20230304/33ab5beb705d7d889965582502e2010e.jpg',


                label395List: [{
                    "name": "一键复制代码"
                }, {
                    "name": "可视化uniapp"
                }, {
                    "name": "自动拖拽布局"
                }, {
                    "name": "一键生成代码"
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
                    success: function(e) {
                        // #ifndef MP
                        that.StatusBar = e.statusBarHeight;
                        if (e.platform == 'android') {
                            that.StatusBar = e.statusBarHeight + 50;
                        } else {
                            that.StatusBar = e.statusBarHeight + 45;
                        };
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


    .flex187 {
        position: relative;
    }

    .flex28188 {
        width: 50%;
        height: 200rpx;
        position: relative;
    }

    .picture929 {
        width: 100%;
    }

    .flex79578 {
        width: 50%;
        position: relative;
    }

    .picture20520 {
        width: 47%;
        margin-top: 4rpx;
        margin-bottom: 4rpx;
        margin-left: 4rpx;
        margin-right: 4rpx;
    }

    .picture70739 {
        width: 47%;
        margin-top: 4rpx;
        margin-bottom: 4rpx;
        margin-left: 4rpx;
        margin-right: 4rpx;
    }

    .picture24724 {
        width: 47%;
        margin-top: 4rpx;
        margin-bottom: 4rpx;
        margin-left: 4rpx;
        margin-right: 4rpx;
    }

    .picture4004 {
        width: 47%;
        margin-top: 4rpx;
        margin-bottom: 4rpx;
        margin-left: 4rpx;
        margin-right: 4rpx;
    }

    .notice395 {
        width: 100%;
        background-color: #ffffff;
    }

    .picture655 {
        width: 48%;
        flex: 1;
    }

    .flex578 {
        width: 49%;
        margin-top: 0rpx;
        margin-bottom: 0rpx;
        margin-left: 18rpx;
        margin-right: 0rpx;
    }

    .picture135 {
        width: 100%;
        margin-top: 0rpx;
        margin-bottom: 6rpx;
        margin-left: 0rpx;
        margin-right: 0rpx;
    }

    .picture28 {
        width: 100%;
        margin-top: 6rpx;
        margin-bottom: 4rpx;
        margin-left: 0rpx;
        margin-right: 0rpx;
    }
</style>
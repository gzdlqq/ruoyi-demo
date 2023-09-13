<template>
  <view>
    <uni-list>
      <uni-list-item v-for="(item,index) in signList" :title="item.name" :note="noteText(item.status)" clickable
                     @click="showDrawer(index)"
                     :thumb="item.bannerUrl"
                     thumb-size="lg" :rightText="item.signTime" showArrow></uni-list-item>
    </uni-list>

    <uni-drawer ref="showRight" mode="mode" :mask-click="true" width="300" :close="closeDrawer">
      <scroll-view style="height: 100%;" scroll-y="true">
        <view>
          <view class="banner">
            <image :src="signList[curSignIndex].bannerUrl" mode="aspectFill"></image>
          </view>
          <view class="content">
            <view class="item">
              <text class="label">赛事名称:</text>
              <text class="value">{{signList[curSignIndex].name}}</text>
            </view>
            <view class="item">
              <text class="label">订单号:</text>
              <text class="value">{{signList[curSignIndex].tradeNo}}</text>
            </view>
            <view class="item">
              <text class="label">报名人:</text>
              <text class="value">{{signList[curSignIndex].playerName}}</text>
            </view>
            <view class="item">
              <text class="label">报名时间:</text>
              <text class="value">{{signList[curSignIndex].signTime}}</text>
            </view>
            <view class="item">
              <text class="label">状态:</text>
              <text class="value">
                {{signList[curSignIndex].status}}
              </text>
            </view>
            <view class="item">
              <text class="label">状态Str:</text>
              <text class="value">
                {{noteText(curStatus)}}
<!--                麻袋，我不知道为什么这样不行，只能用data 临时存储了-->
<!--                {{noteText(signList[curSignIndex].status)}}-->
              </text>
            </view>
            <view class="item">
              <text class="label">报名费用:</text>
              <text class="value">¥{{signList[curSignIndex].totalFee}}</text>
            </view>
            <button @click="continueToPay">继续支付</button>
            <button @click="querySignBasePay">查询是否已经支付</button>
          </view>
        </view>
      </scroll-view>
    </uni-drawer>
  </view>
</template>

<script>
import {queryMySign,paySuccess,querySignBasePay} from '@/api/system/sign.js';
import {continueToPay} from '@/api/system/pay.js';

export default {
  name: "mysign",
  data() {
    return {
      //当前详情的id
      signList: [],
      curSignIndex: null,
      curStatus: null
    };
  },
  onShow() {
    this.queryMySign()
  },
  methods: {
    //打开抽屉
    showDrawer(index) {
      this.$refs.showRight.open();
      this.curSignIndex = index;
      this.curStatus = this.signList[index].status
      // debugger
      console.log('点击了'+this.curSignIndex)
    },
    async queryMySign() {
      let res = await queryMySign();
      this.signList = res.data
    },
    noteText(note) {
        switch (note) {
          case '0':
            return '未付款';
          case '1':
            return '已付款(待审核)';
          case '2':
            return '报名成功';
          case '3':
            return '订单关闭';
		case '4':
		  return '审核拒绝(退款)';
          default:
            return '未知';
        }
    },
    async continueToPay(){
      let tradeNo = this.signList[this.curSignIndex].tradeNo
      console.log(tradeNo)

      //继续支付 5个参数
      let orderResp = await continueToPay(tradeNo.toString());

      const res = await uni.requestPayment({
        provider: 'wxpay',
        timeStamp: orderResp.data.timeStamp,
        nonceStr: orderResp.data.nonceStr,
        package: orderResp.data.packageValue,
        signType: 'MD5',
        paySign: orderResp.data.paySign,
        success: async function (res) {
          // debugger
          console.log('success:' + JSON.stringify(res));
          //订单号
          let tradeNo = orderResp.tradeNo;
          // _this.tradeNo = orderResp.tradeNo

          let paySuccessRes = await paySuccess(tradeNo);

          uni.showModal({
            title: '提示',
            content: paySuccessRes.msg,
            success: function (res) {
              if (res.confirm) {
                console.log('用户点击确定');
              } else if (res.cancel) {
                console.log('用户点击取消');
              }
            }
          });

        },
        fail: function (err) {
          console.log('fail:' + JSON.stringify(err));
        }
      });

    },
    //查询订单是否支付
    async querySignBasePay(){
      let tradeNo = this.signList[this.curSignIndex].tradeNo
      let resp = await querySignBasePay(tradeNo.toString());
      this.$modal.showToast(resp.tradeStateDesc)
      console.log(resp)
    }
  },
}
</script>

<style scoped>

</style>

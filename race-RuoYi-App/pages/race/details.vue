<template>
  <div class="race-details">
    <div class="race-banner">
      <img :src="race.bannerUrl" alt="race banner"/>
    </div>
    <div class="race-info">
      <h1 class="race-name">{{ race.name }}</h1>
      <div class="race-time">
        <div>
          <h3>报名时间</h3>
          <p>开始时间：{{ formatDate(race.registrationStartTime) }}</p>
          <p>结束时间：{{ formatDate(race.registrationEndTime) }}</p>
        </div>
        <div>
          <h3>比赛时间</h3>
          <p>开始时间：{{ formatDate(race.startTime) }}</p>
          <p>预计结束时间：{{ formatDate(race.estimatedEndTime) }}</p>
        </div>
      </div>
      <div class="race-location">
        <h3>赛事地点</h3>
        <p>经度：{{ race.longitude }}</p>
        <p>纬度：{{ race.latitude }}</p>
      </div>
      <div class="race-location">
        <h3>价格</h3>
        <p>{{ race.raceSchema.price }}</p>
      </div>
      <div class="race-details">
        <h3>赛事介绍</h3>
        <p>{{ race.description }}</p>
      </div>
      <button type="default" v-if="race.raceSchema.price > 0" plain="true" size="mini" @click="onPay">去支付报名</button>
      <button type="default" v-else plain="true" size="mini">免费报名</button>
      <button type="default" open-type="share" plain="true" size="mini">分享给朋友</button>
    </div>
  </div>
</template>

<script>
import {getRaceDetails} from '@/api/system/race.js';
import {createPayOrder} from '@/api/system/pay.js';
import {paySuccess} from '@/api/system/sign.js';


export default {
  name: "RaceDetails",
  onLoad(options) {
    console.log('详情页面接受参数', options.id)
    this.id = options.id
    this.getRaceDetails();


    // 调用获取定位权限函数
    this.getLocationAuth();


  },
  data() {
    return {
      //当前详情的id
      id: null,
      race: {
        latitude: null,
        longitude: null
      },
      curPersonPosition: {
        latitude: null,
        longitude: null
      },
      tradeNo: null
    };
  },
  methods: {

    // 设置分享方法
    onShareAppMessage(res) {
      if (res.from === 'button') {// 来自页面内分享按钮
        console.log(res.target)
      }
      return {
        title: '自定义分享标题,快来一起参加吧',
        path: '/pages/race/details?id=' + this.id
      }
    },
    getLocation() {
      let _this = this
      // debugger
      uni.getLocation({
        type: 'gcj02',
        success: function (res) {
          console.log('获取位置信息成功：', res);
          _this.curPersonPosition.latitude = res.latitude
          _this.curPersonPosition.longitude = res.longitude

          //调用成功之后，计算距离
          const distance = _this.distance(_this.curPersonPosition.latitude, _this.curPersonPosition.longitude, _this.race.latitude, _this.race.longitude)

          // uni.showModal({
          //   title: '提示',
          //   content: '当前您的位置 距离 比赛的地方 '+distance+' km',
          //   success: function (res) {
          //     if (res.confirm) {
          //       console.log('用户点击确定');
          //     } else if (res.cancel) {
          //       console.log('用户点击取消');
          //     }
          //   }
          // });


        },
        fail: function (err) {
          console.log('获取位置信息失败：', err);
        }
      });
    },
    getLocationAuth() {
      let _this = this
      uni.authorize({
        scope: 'scope.userLocation',
        success: function () {
          console.log('用户同意定位权限');
          _this.getLocation();
        },
        fail: function (err) {
          console.log('获取定位权限失败：', err);
        }
      });
    },

    distance(lat1, lon1, lat2, lon2) {
      const R = 6371; // 地球半径（单位：千米）
      const dLat = (lat2 - lat1) * Math.PI / 180;
      const dLon = (lon2 - lon1) * Math.PI / 180;
      const a =
          Math.sin(dLat / 2) * Math.sin(dLat / 2) +
          Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
          Math.sin(dLon / 2) * Math.sin(dLon / 2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      const distance = R * c;
      return distance; // 返回两个坐标之间的距离（单位：千米）
    },

    // 格式化日期时间
    formatDate(dateTime) {
      return dateTime ? new Date(dateTime).toLocaleDateString() : "-";
    },
    // 获取赛事详情ipcofn
    getRaceDetails() {
      getRaceDetails(this.id).then(response => {
        this.race = response.data
      })
    },
    async onPay() {
      let _this = this;
      uni.requestSubscribeMessage({
        tmplIds: ['5U67v-dtQCNUzWoJhHL-vbNyzXDwwKEoo7ZKEVE7JRI'],
        async success(result) {

          //后台调用 生成支付订单
          const orderResp = await createPayOrder(_this.race.id);
          // debugger
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

              let paySuccessRes = await paySuccess(tradeNo.toString());

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
        }
      })


    }
  }
};
</script>

<style scoped>
.race-banner {
  text-align: center;
}

.race-banner img {
  width: 100%;
  max-width: 800px;
}

.race-info {
  margin: 20px 0;
  padding: 0 20px;
  font-size: 16px;
}

.race-info h1 {
  font-size: 24px;
}

.race-time,
.race-location,
.race-details {
  margin: 20px 0;
}

.race-time h3,
.race-location h3,
.race-details h3 {
  font-size: 20px;
  margin-bottom: 10px;
}

.race-time div,
.race-location div,
.race-details p {
  margin-bottom: 5px;
}
</style>

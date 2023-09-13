<template>
  <div class="event-block">
    <div class="banner-image" :style="{ backgroundImage: 'url(' + bannerUrl + ')' }"></div>
    <div class="event-name">{{ eventName }}</div>
    <div class="event-info">
      <div @click="xx" class="event-fee">费用：{{ eventFee }}元</div>
      <div class="event-registration-time">报名时间：{{ registrationStartTime }} - {{registrationEndTime}}</div>
      <div class="event-time">比赛时间：{{ eventStartTime }}</div>
      <div class="event-status">状态：<span :class="statusClass">{{ eventStatusText }}</span></div>
      <button type="primary" plain="true" @click="gotoDetail">查看详情</button>
    </div>
  </div>
</template>


<script>
export default {
  props: {
    cid: {
      type: Number,
      required: true
    },
    bannerUrl: {
      type: String,
      required: true
    },
    eventName: {
      type: String,
      required: true
    },
    eventFee: {
      type: Number,
      required: true
    },
    registrationStartTime: {
      type: String,
      required: true
    },
    registrationEndTime: {
      type: String,
      required: true
    },
    eventStartTime: {
      type: String,
      required: true
    },
    eventStatus: {
      type: Number,
      required: true,
      validator: function (value) {
        return [1, 2, 3, 4, 5].includes(value);
      },
    },
  },
  methods:{
    gotoDetail() {
      // debugger
      // console.log('跳转详情',this.cid)
      uni.navigateTo({
        url: `/pages/race/details?id=${this.cid}`,
      });
    },
  },
  computed: {
    eventStatusText: function () {
      switch (this.eventStatus) {
        case 1:
          return "即将开始";
        case 2:
          return "报名中";
        case 3:
          return "编排中";
        case 4:
          return "比赛中";
        case 5:
          return "已结束";
        default:
          return "未知状态";
      }
    },
    statusClass: function () {
      switch (this.eventStatus) {
        case 1:
          return "status-will-start";
        case 2:
          return "status-registering";
        case 3:
          return "status-arranging";
        case 4:
          return "status-playing";
        case 5:
          return "status-ended";
        default:
          return "";
      }
    },
  },
};
</script>

<style scoped>
.event-block {
  background-color: #fff;
  border-radius: 10px;
  border: 2px solid #eee;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  padding: 20px;
  color: #333; /* 调整文字颜色 */
}

.event-status {
  font-size: 16px;
  margin-bottom: 5px;
  color: #666; /* 调整文字颜色 */
}

.event-status .status-will-start {
  color: #2196f3;
}

.event-status .status-registering {
  color: #4caf50;
}

.event-status .status-arranging {
  color: #ff9800;
}

.event-status .status-playing {
  color: #f44336;
}

.event-status .status-ended {
  color: #9e9e9e;
}
.banner-image {
  width: 100%;
  height: 150px;
  background-position: center;
  background-size: cover;
}
</style>

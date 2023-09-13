<template>
  <view>
    <view v-on:click="gotoDetail()">我留着就是让你对比下生命周期的下拉</view>
    <br>
    <br>
    <br>

    <scroll-view scroll-y="true" @scrolltolower="loadNextPage" lower-threshold="50"
                 style="height: 100vh"
                 refresher-enabled="true"
                 @refresherpulling="onPulling"
                 @refresherrefresh="onRefresh"
                 @refresherrestore="onRestore"
                 @refresherabort="onAbort"
                 :refresher-triggered="triggered">
      <RaceItem
          v-for="(item, index) in raceList"
          :key="item.id"
          :cid="item.id"
          :event-name="item.name"
          :event-fee="item.raceSchema.price"
          :registration-start-time="item.registrationStartTime"
          :registration-end-time="item.registrationEndTime"
          :event-start-time="item.startTime"
          :event-status="item.status"
          :bannerUrl="item.bannerUrl"
      />
      <view v-if="isLastPage" style="text-align: center">
        <text>已经到底了</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import RaceItem from "./components/race-item.vue";
import {pageRace} from '@/api/system/race.js';

export default {
  components: {
    RaceItem,
  },
  data() {
    return {
      loading: false,
      isLastPage: false, // 标志变量
      page: {
        pageNum: 0,
        pageSize: 10
      },
      raceQuery: {},
      raceList: [],
      triggered: false // 设置当前下拉刷新状态，true 表示下拉刷新已经被触发，false 表示下拉刷新未被触发
    };
  },
  computed: {},
  methods: {
    loadNextPage() {
      console.log('xxx')
      //是否最后一页了
      if (this.isLastPage) {
        return;
      }

      //如果处在加载中，防止二次加载，必须等待这一次加载完
      if (this.loading) {
        return;
      }

      uni.showLoading({
        title: '加载中'
      });

      this.loading = true

      this.page.pageNum++;

      pageRace(this.page, this.raceQuery).then(response => {
        if (response.data.list.length > 0) {
          // 数组合并   ...是叫做展开运算符
          this.raceList = [...this.raceList, ...response.data.list];
        } else {
          this.isLastPage = true; // 到达最后一页
          uni.showToast({
            title: '已经到底了',
            duration: 2000
          });
        }
        uni.hideLoading();
        this.loading = false
      }).catch(error => {
        // debugger
        this.$modal.msgError(error);
        this.page.pageNum--;
        uni.hideLoading();
        this.loading = false
      });
    }
    ,
    onPulling() {
      // 下拉刷新操作 这个只要下拉 一直会触发
      // console.log('自定义下拉刷新控件被下拉')
      this.triggered = true
    },
    onRefresh() {
      // 下拉刷新操作  拉下来之后 挂在那边转圈
      console.log('自定义下拉刷新被触发')

      //到时候 肯定是在 这个地方写 业务代码 代码 （重新加载第一页）
      this.page.pageNum = 0
      //先清空 list数据，效果明显一点
      this.raceList = []
      this.loading = false
      this.isLastPage = false

      this.loadNextPage()

      // 这个动作就是让他复位
      this.triggered = false

    }
    ,
    onRestore() {
      // 自定义下拉刷新被复位
      console.log('自定义下拉刷新被复位')
    },
    onAbort() {
      // 自定义下拉刷新被中止
      console.log('自定义下拉刷新被中止')
    }
    ,
    //配置的下拉刷新监听  仅仅是例子  没有实际用到
    onPullDownRefresh() {
      uni.showToast({
        title: '配置的下拉刷新监听',
        duration: 2000
      });

      setTimeout(function () {
        uni.stopPullDownRefresh();
      }, 3000);
    }
  },
  onLoad() {
    //初始化 加载 第一页数据
    this.loadNextPage();
  },
  onPullDownRefresh() {
    console.log('下拉')
  }
};
</script>

<style lang="scss">

</style>

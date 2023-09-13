<template>
  <view class="container">
    <view class="auth-header">
      <view class="auth-logo">
        <avatar size="100" icon="github-circle-fill" fontSize="100"></avatar>
      </view>
    </view>

    <view class="auth-box">
      <view class="btn-group">
        <!-- #ifdef MP-WEIXIN -->
        <button type="warn" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">一键登录</button>
        <!-- #endif -->
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {}
  },
  onLoad() {
  },
  onReady() {
  },
  methods: {
    getPhoneNumber(e) {
      let phoneCode = e.detail.code
      console.log('获取了手机号的临时票据:' + phoneCode)
      // 当我们拒绝走走这个分支
      if (!e.detail.code) {
        uni.showModal({
          title: '授权失败',
          content: '您已拒绝获取绑定手机号登录授权，可以使用其他手机号验证登录',
          cancelText: '知道了',
          confirmText: '验证登录',
          confirmColor: '#3C9CFFFF',
          success: res => {
            if (res.confirm) {
              // uni.$u.route('/pages/login/mobile')
            } else if (res.cancel) {
              //console.log('用户点击取消')
            }
          }
        })
      } else {
        //同意走这里
        uni.login({
          provider: 'weixin',
          success: res => {
            this.$store.dispatch('Login', {type: 2, data: {phoneCode: phoneCode, loginCode: res.code}}).then(res => {
              this.$modal.msgError("登录成功")
              this.loginSuccess()
            })
          }
        })
      }
    },
    // 登录成功后，处理函数
    loginSuccess(result) {
      // 设置用户信息
      this.$store.dispatch('GetInfo').then(res => {
        console.log(1)
        const backUrl = uni.getStorageSync('backUrl')
        if (backUrl) {
          uni.redirectTo({
            url: backUrl
          })
          uni.removeStorageSync('backUrl')
        } else {
          this.$tab.reLaunch('/pages/index');
        }

      });
    }
  }
}
</script>

<style lang="scss" scoped>

</style>

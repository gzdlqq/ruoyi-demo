import config from '@/config'
import storage from '@/utils/storage'
import constant from '@/utils/constant'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const baseUrl = config.baseUrl

const user = {
  state: {
    token: getToken(),
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions)
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
      storage.set(constant.name, name)
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
      storage.set(constant.avatar, avatar)
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
      storage.set(constant.roles, roles)
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
      storage.set(constant.permissions, permissions)
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const phoneCode = userInfo.data.phoneCode
      const loginCode = userInfo.data.loginCode
      // debugger

      return new Promise((resolve, reject) => {
        login(phoneCode, loginCode).then(res => {
          // setToken(res.token) //存储到 storage 这个不变
          setToken(res.data.wxToken) //存储到 storage 这个不变
          // commit('SET_TOKEN', res.token) //将返回的token 放在vuex中管理
          commit('SET_TOKEN', res.data.wxToken) //将返回的token 放在vuex中管理
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          // debugger
          const user = res.data
          const avatar = (user == null || user.avatar == "" || user.avatar == null) ? require("@/static/images/profile.jpg") : baseUrl + user.avatar
          const username = (user == null || user.nickname == "" || user.nickname == null) ? "" : user.nickname

          // C端没有角色的概念
          // if (res.roles && res.roles.length > 0) {
          //   commit('SET_ROLES', res.roles)
          //   commit('SET_PERMISSIONS', res.permissions)
          // } else {
          //   commit('SET_ROLES', ['ROLE_DEFAULT'])
          // }
          commit('SET_NAME', username)
          commit('SET_AVATAR', avatar)
          resolve(res)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          removeToken()
          storage.clean()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user

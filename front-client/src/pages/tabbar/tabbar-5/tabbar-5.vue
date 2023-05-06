<template>
  <view class="content">
    页面 - 5
  </view>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket'

export default {
  data() {
    return {
      title: 'Hello',
      heartbaet: null,
    }
  },
  onLoad() {
    this.initSocket()
  },
  methods: {
    initSocket() {
      let that = this
      console.log('开始初始化socket连接')

      var socket = new ReconnectingWebSocket(
          `ws://${location.host}${process.env.NODE_ENV === 'production' ? '/videoMonitor' : ''
          }//wsserver/111`
      )
      socket.debug = true
      socket.onopen = () => {
        console.log('开始建立socket连接')
        if (this.heartbaet != null) clearInterval(this.heartbaet)
        //定时发送心跳包
        this.heartbaet = setInterval(() => {
          let r = Date.parse(new Date())
          let heartBeatMsg = that.buildSocketReq('HEART_BEAT', '^h&b^' + r)
          this.socket.send(JSON.stringify(heartBeatMsg))
        }, 1000 * 25)
      }
      socket.onmessage = e => {
        let response = JSON.parse(e.data)
        if (response.result.indexOf('^s^h&b^') > -1 || response.type === 'HEART_BEAT') {

        } else {
          if (e.data && e.data.length > 300) {
            this.log('onmessage:' + e.data.substr(0, 300))
          } else {
            this.log('onmessage:' + e.data)
          }

        }
      }

      socket.onclose = e => {
        if (e && e.reason) {
          this.log('ws关闭,error:' + e.reason)
        } else {
          this.log('ws关闭,error:' + e)
        }
      }
      this.socket = socket
    },
    //生成uid
    generateUid() {
      return Number(Math.random().toString().substr(3, 5) + Date.now()).toString(16)
    },
    buildSocketReq(cmdEnterType, cmd) {
      let socketReq = {
        uniKey: this.generateUid(),
        cmd: cmd,
        cmdEnterType: cmdEnterType
      }
      return socketReq
    },

  }
}
</script>

<style>
.content {
  text-align: center;
  height: 400 upx;
  margin-top: 200 upx;
}
</style>

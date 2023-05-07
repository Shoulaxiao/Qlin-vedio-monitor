<template>
  <view>
    <uni-section title="湿度" titleFontSize="18px" type="line">
      <view class="charts-box">
        <qiun-data-charts
            type="arcbar"
            :opts="opts"
            :chartData="chartData"/>
      </view>
    </uni-section>

    <uni-section title="湿度" titleFontSize="18px" type="line">
      <view class="charts-box">
        <qiun-data-charts
            type="gauge"
            :opts="optsTemperature"
            :chartData="chartDataTemperature"
        />
      </view>
    </uni-section>

  </view>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket'

export default {
  data() {
    return {
      title: 'Hello',
      heartbaet: null,
      chartData: {},
      chartDataTemperature: {},
      opts: {
        color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#ea7ccc"],
        padding: undefined,
        title: {
          name: "80%",
          fontSize: 35,
          color: "#2fc25b"
        },
        subtitle: {
          name: "湿度",
          fontSize: 25,
          color: "#666666"
        },
        extra: {
          arcbar: {
            type: "default",
            width: 12,
            backgroundColor: "#E9E9E9",
            startAngle: 0.75,
            endAngle: 0.25,
            gap: 2,
            linearType: "custom"
          }
        }
      },
      optsTemperature: {
        color: ["#1890FF", "#91CB74", "#FAC858", "#EE6666", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#ea7ccc"],
        padding: undefined,
        title: {
          name: "60℃",
          fontSize: 25,
          color: "#2fc25b",
          offsetY: 50
        },
        subtitle: {
          name: "当前温度",
          fontSize: 15,
          color: "#666666",
          offsetY: -50
        },
        extra: {
          gauge: {
            type: "default",
            width: 30,
            labelColor: "#666666",
            startAngle: 0.75,
            endAngle: 0.25,
            startNumber: 0,
            endNumber: 100,
            labelFormat: "",
            splitLine: {
              fixRadius: 0,
              splitNumber: 10,
              width: 30,
              color: "#FFFFFF",
              childNumber: 5,
              childWidth: 12
            },
            pointer: {
              width: 24,
              color: "auto"
            }
          }
        }
      }
    }
  },
  onReady() {
    this.getServerData();
    this.initSocket()

  },
  onLoad() {
    // this.initSocket()
  },
  methods: {
    getServerData() {
      //模拟从服务器获取数据时的延时
      setTimeout(() => {
        //模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
        let res = {
          series: [
            {
              name: "湿度",
              color: "#2fc25b",
              data: 0.8
            }
          ]
        };
        this.chartData = JSON.parse(JSON.stringify(res));


        //模拟服务器返回数据，如果数据格式和标准格式不同，需自行按下面的格式拼接
        let resTemperature = {
          categories: [{"value": 0.2, "color": "#1890ff"}, {"value": 0.8, "color": "#2fc25b"}, {
            "value": 1,
            "color": "#f04864"
          }],
          series: [
            {
              name: "温度",
              data: 0.66
            }
          ]
        }
        this.chartDataTemperature = JSON.parse(JSON.stringify(resTemperature));
      }, 500);
    },
    initSocket() {
      let that = this
      console.log('开始初始化socket连接')

      var socket = new ReconnectingWebSocket(
          `ws://${location.host}${process.env.NODE_ENV === 'production' ? '/videoMonitor' : ''
          }/wsserver/111`
      )
      socket.debug = true
      socket.onopen = () => {
        console.log('开始建立socket连接')
        if (this.heartbaet != null) clearInterval(this.heartbaet)
        let firstSensor = that.buildSocketReq('SENSOR_INFO', '^h&b^')

        this.socket.send(JSON.stringify(firstSensor))

        //定时发送心跳包
        this.heartbaet = setInterval(() => {
          let r = Date.parse(new Date())
          let heartBeatMsg = that.buildSocketReq('HEART_BEAT', '^h&b^' + r)
          this.socket.send(JSON.stringify(heartBeatMsg))
        }, 1000 * 25)
      }
      socket.onmessage = e => {
        if (e.data == '连接成功') return
        let response = JSON.parse(e.data)
        if (response.result.indexOf('^s^h&b^') > -1 || response.type === 'HEART_BEAT') {

        } else {
          if (e.data) {
            let dataRes = JSON.parse(e.data)
            if (!!dataRes.result) {
              let sensorInfo = JSON.parse(dataRes.result)
              let res = {
                series: [
                  {
                    name: "湿度",
                    color: "#2fc25b",
                    data: Number(sensorInfo.humidity)
                  }
                ]
              }
              setTimeout(() => {
                this.chartData = JSON.parse(JSON.stringify(res));
              }, 500);
            }
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
};

</script>

<style>
.content {
  /*width: 100%;*/
  /*height: 300px;*/
}

.humidty-canvase {
  width: 50%;
  margin: 20 rpx auto;
}

.canvas {
  width: 100%;
  height: 300px;
}
</style>

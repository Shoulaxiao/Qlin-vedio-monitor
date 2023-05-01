<template>
  <view class="page-body">
    <view class="video-content">
      <view class="content">
        <view id="dplayer"></view>
      </view>
    </view>

    <view class="page-item">
      <view class="btn-area">
        <uni-list :border="false">
          <uni-list-item :border="false" title="动态瞬间" note="拍摄到的重要事件" @click="goDynamicTime" link>
            <template v-slot:header>
              <uni-icons class="t-icon t-icon-bofang1" type="con-bofang1"/>
            </template>
          </uni-list-item>

          <uni-list-item :border="false" title="存储管理" note="删除历史记录视频" link>
            <template v-slot:header>
              <uni-icons class="t-icon t-icon-bofang1"/>
            </template>
          </uni-list-item>
        </uni-list>
      </view>
    </view>
  </view>


</template>

<script>
import api from '@/api/videoService'
//引入 hls与dplayer 用于解析播放视频
import Hls from 'hls.js'
import Dplayer from 'dplayer'
export default {
  data() {
    return {
      title: 'Hello',
      src: '',
      videoSrc: '',
      dp: {}
    }
  },
  async mounted() {
    await this.getLiveUrl()
    this.dp = new Dplayer({
      //播放器的一些参数
      container: document.getElementById('dplayer'),
      autoplay: true, //是否自动播放
      theme: '#FADFA3', //主题色
      loop: true,//视频是否循环播放
      lang: 'zh-cn',
      screenshot: false,//是否开启截图
      hotkey: true,//是否开启热键
      preload: 'auto',//视频是否预加载
      volume: 0.7,//默认音量
      mutex: true,//阻止多个播放器同时播放，当前播放器播放时暂停其他播放器
      video: {
        url: this.videoSrc, //视频地址
        type: 'customHls',
        customType: {
          customHls: function(video, player) {
            const hls = new Hls()  //实例化Hls  用于解析m3u8
            hls.loadSource(video.src)
            hls.attachMedia(video)
          }
        },
      },
    });
    console.log(this.dp)

  },
  onLoad(option) {
  },

  methods: {
    async getLiveUrl() {
      let res = await api.getLiveUrl()
      if (res && res.success) {
        setTimeout(()=>{
          this.videoSrc = res.data
        });
      }
    },

    goDynamicTime() {
      uni.navigateTo({
        url: '/pages/dynamic/index'
      })
    }
  }
};
</script>
<style lang="scss">

.page-body {
  background-color: #f8f8f8;
}

.page-item {
  margin-top: 100px;
}

.video-content {
  background-color: #ffffff;
  height: 300px;
  border-bottom-right-radius: 25px;
  border-bottom-left-radius: 25px;

}

.content {
  text-align: center;
  height: 25%;
  width: 100%;
}

$uni-success: #18bc37 !default;

.uni-wrap {
  flex-direction: column;
  /* #ifdef H5 */
  height: calc(100vh - 44px);
  /* #endif */
  /* #ifndef H5 */
  height: 100vh;
  /* #endif */
  flex: 1;
}

.mb-10 {
  margin-bottom: 10px;
}

.decoration {
  width: 6px;
  height: 6px;
  margin-right: 4px;
  border-radius: 50%;
  background-color: $uni-success;
}

</style>
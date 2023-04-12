<template>
  <view name="videoPlayer" class="flex flex-direction">
    <video id="myVideo"
           class="response"
           :src="videoUrl"
           :autoplay="autoPlay"
           :title="videoTitle"
           :controls="true"
           object-fit="contain"
           show-mute-btn="true"
           enable-play-gesture="true"
           vslide-gesture="true"
           @error="videoErrorCallback"
           @waiting="videoWaiting"
           @loadedmetadata="videoLoadOk"
           :is-live="isLive"
    ></video>
  </view>
</template>

<script>
export default {
  name: "videoPlayer",
  props: {
    title: {
      type: String,
      default: ''
    },
    firstPic: {
      type: String,
      default: ''
    },
    isLive:{
      type:Boolean,
      default:false,
    },
    videoType: {
      type: String,
      default: ''
    },
    videoSrc: {
      type: String,
      default: ''
    },
    autoPlay:{
      type:Boolean,
      default:false
    }
  },
  data() {
    return {
      videoUrl: this.videoSrc,
      firstImg: this.firstPic,
      videoTitle: this.title
    };
  },
  watch: {
    videoSrc() {
      this.setVideoUrl();
    }
  },
  mounted() {
    this.setVideoUrl();
  },

  onReady: function(res) {
    // #ifndef MP-ALIPAY
    this.videoContext = uni.createVideoContext('myVideo');
    // #endif
  },
  methods: {
    setVideoUrl() {
      this.videoUrl = this.videoSrc;
      this.firstImg = this.firstPic;
      this.videoTitle = this.title;
    },
    videoErrorCallback(e) {
      // uni.showModal({
      //   content: e.target.errMsg,
      //   showCancel: false
      // });
    },
    videoWaiting() {
      // uni.showLoading({
      //     title: '加载中'
      // });
    },
    videoLoadOk() {
      uni.hideLoading();
    }
  }
}
</script>

<style scoped>
  .response{
    width: 100%;
  }
</style>
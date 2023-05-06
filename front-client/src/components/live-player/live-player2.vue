<template>
  <div id="dplayer"></div>
</template>

<script>
import DPlayer from "dplayer";
export default {
  name:'livePlayer2',
  props: {
    autoplay: {
      type: Boolean,
      default: false,
    },
    theme: {
      type: String,
      default: "#FADFA3",
    },
    loop: {
      type: Boolean,
      default: false,
    },
    lang: {
      type: String,
      default: "zh",
    },
    screenshot: {
      type: Boolean,
      default: false,
    },
    hotkey: {
      type: Boolean,
      default: true,
    },
    preload: {
      type: String,
      default: "auto",
    },
    contextmenu: {
      type: Array,
    },
    logo: {
      type: String,
    },
    video: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      dp: null,
    };
  },
  mounted() {
    const player = (this.dp = new DPlayer({
      // 挂载元素
      container: document.getElementById('dplayer'),
      // 自动播放
      autoplay: this.autoplay,
      // 主题颜色
      theme: this.theme,
      // 循环播放
      loop: this.loop,
      // 显示语言
      lang: this.lang,
      // 显示抓拍
      screenshot: this.screenshot,
      // 快捷键
      hotkey: this.hotkey,
      // 预加载
      preload: this.preload,
      // 播放器上下文菜单
      contextmenu: this.contextmenu,
      // 左上角logo
      logo: this.logo,
      // 播放视频源
      video: {
        // 播放器背景图
        pic: this.video.pic,
        // defaultQuality: this.video.q,
        // 不同播放质量源
        quality: [
          {
            url: this.video.quality[0].url,
            name: this.video.quality[0].name,
          }
        ],
        // 缩略图
        thumbnails: this.video.thumbnails,
        // 视频源类型
        type: this.video.type,
      },
    }));
    // 播放事件
    player.on("play", () => {
      this.$emit("play");
    });
    // 停止事件
    player.on("pause", () => {
      this.$emit("pause");
    });
    // 可播放事件
    player.on("canplay", () => {
      this.$emit("canplay");
    });
    // 正在播放事件
    player.on("playing", () => {
      this.$emit("playing");
    });
    // 结束播放事件
    player.on("ended", () => {
      this.$emit("ended");
    });
    // 播放错误事件
    player.on("error", () => {
      this.$emit("error");
    });
  }
};
</script>
<style scoped></style>

<template>
  <view class="all-content">
    <view class="content">
      <video-player :video-src="currentPlay.url" :autoPlay="true"></video-player>
      <view>
        <uni-list :border="false">
          <uni-list-item class="video-detail" :border="false" :title="currentPlay.title"
                         :note="$moment(currentPlay.createTime).format('YYYY-MM-DD HH:mm。')+currentPlay.description">
            <template v-slot:header>
              <uni-icons :class="`t-icon ${getTypeIcon(currentPlay.type)}`"/>
            </template>
          </uni-list-item>
        </uni-list>
      </view>

      <view class="data-title">{{ `${$moment(currentPlay.createTime).format("YY年MM月DD日")}` }}</view>

      <uni-list class="video-list" :border="true">
        <view class="video-play-list-item" v-for="item in videoList">
          <view class="being-playing" v-if="currentPlay.id === item.id">正在播放</view>
          <uni-list-item @click="changePlay(item)" clickable direction="row" :key="item.id" :title="item.title"
                         :note="$moment(item.createTime).format('HH:mm')">
            <template class="video-item-footer" v-slot:header>
              <!-- 当前判断长度只为简单判断类型，实际业务中，根据逻辑直接渲染即可 -->
              <uni-icons :class="`t-icon2 ${getTypeIcon(item.type)}`"/>
            </template>
            <template v-slot:footer>
              <!-- 当前判断长度只为简单判断类型，实际业务中，根据逻辑直接渲染即可 -->
              <image :class="`${currentPlay.id === item.id?'image-2':'image-1'}`" :src="item.preViewImgUrl"
                     mode="aspectFill"></image>
            </template>
          </uni-list-item>
        </view>
        <uni-load-more :contentText="contentText" :showIcon="true" :showText="true" :status="status"
                       @clickLoadMore="clickLoadMore"></uni-load-more>

      </uni-list>
    </view>

  </view>
</template>

<script>
import calendar from 'components/calendar/calendar'
import customList from 'components/customlist/custom-list'
import api from '@/api/videoService'
import VideoPlayer from "@/components/video-player/video-player";

export default {
  components: {VideoPlayer, calendar, customList},
  name: "detail",
  data() {
    return {
      videoList: [],
      contentText:
          {contentdown: "点击加载更多", contentrefresh: "正在加载...", contentnomore: "没有更多数据了"}
      ,
      currentPlay: {
        url: '',
        type: -1,
        preViewImgUrl: '',
        id: '',
        title: '',
        description: '',
        createTime: this.$moment()
      },
      status: 'noMore',
      page: 1,
      pageSize: 10,
    }
  },

  async onLoad(option) {
    await this.queryVideoDetail(option.id)
    this.queryVideoPage()

  },
  methods: {
    changePlay(item) {
      uni.navigateTo({
        url: '/pages/dynamic/detail?id=' + item.id
      })
    },

    async clickLoadMore(e) {
      this.status = 'loading'
      let param = {
        pageSize: this.pageSize,
        page: this.page + 1,
        date: this.currentPlay.createTime,
      }
      let res = await api.queryVideosListPage(param)
      if (res.success) {
        this.videoList = res.values
        this.showMoreButton(res.pageInfo.items)
      }
    },

    getTypeIcon(type) {
      if (type === 0) {
        return 't-icon-shoucang'
      } else {
        return 't-icon-youhuiquan'
      }
    },

    async queryVideoDetail(id) {
      let res = await api.queryDetailById(id)
      if (res.success) {
        this.currentPlay = res.data
        console.log("目前的视频播放地址:", this.currentPlay.url)
      }
    },
    async queryVideoPage() {
      let param = {
        pageSize: this.pageSize,
        page: 1,
        date: this.currentPlay.createTime,
      }
      let res = await api.queryVideosListPage(param)
      if (res.success) {
        this.videoList = this.videoList.concat(res.values)
        this.showMoreButton(res.pageInfo.items)
      }
    },

    showMoreButton(total = 0) {
      if (this.videoList.length < total) {
        this.status = 'more'
      } else {
        this.status = 'noMore'
      }
    },

    /**
     * 下拉刷新回调函数
     */
    onPullDownRefresh() {
      console.log("下拉刷新")
      var that = this
      setTimeout(function () {
        // 顶部提示
        let options = {
          msg: "加载成功~",
          duration: 2000,
          type: "green"
        };
        that.$refs.toast.showTips(options);

        //隐藏转圈动画
        uni.stopPullDownRefresh()
      }, 2000)

    },
    /**
     * 上拉加载更多
     */
    onReachBottom() {
      // 显示加载中
      this.statusLoadMore = "loading"
      var that = this

      setTimeout(function () {
        // 显示没有更多数据了
        that.statusLoadMore = "nomore"
      }, 2000)
    }
  }
}
</script>

<style lang="scss" scoped>

.all-content {
  background-color: #f8f8f8;
}

.chat-custom-right {
  flex: 1;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
}

.chat-custom-text {
  font-size: 12px;
  color: #999;
}

.image-1 {
  flex-shrink: 0;
  margin-right: 10px;
  width: 125px;
  height: 75px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px #f5f5f5 solid;
}

.image-2 {
  flex-shrink: 0;
  margin-right: 10px;
  width: 125px;
  height: 75px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px #f5f5f5 solid;
  background: #efefef;
  -moz-opacity: 0.8;
  opacity: 0.8;
  -webkit-filter: blur(1px); /* Chrome, Opera */
  -ms-filter: blur(1px);
  filter: blur(1px);
}

.data-title {
  width: 100%;
  opacity: 0.6;
  align-items: center;
  //padding: 10px 5%;
  font-size: 12px;
  font-weight: bold;
  margin-left: 38px;
  text-align: left;
  margin-top: 25px;
  margin-bottom: 5px;
  /* box-shadow: 2px 0 5px rgba(0,0,0,.4); */
  color: #212020;
}

.video-list {
  margin-top: 20px;
}

.video-detail {
  //font-weight: bold;
  font-size: 12px;
}

.data-title {
  width: 100%;
  opacity: 0.6;
  align-items: center;
  //padding: 10px 5%;
  font-size: 12px;
  font-weight: bold;
  margin-left: 38px;
  text-align: left;
  margin-top: 25px;
  margin-bottom: 5px;
  /* box-shadow: 2px 0 5px rgba(0,0,0,.4); */
  color: #212020;
  letter-spacing: 1px;
}

.video-play-list-item {
  position: relative;
}

.video-play-list-item .uni-list-item .uni-list-item__container .uni-list-item__content {
  display: flex;
  padding-right: 8px;
  flex: 1;
  color: #3b4144;
  flex-direction: column;
  //justify-content: space-between;
  overflow: hidden;
}

.being-playing {
  position: absolute;
  z-index: 999;
  font-size: 12px;
  top: 41px;
  left: 273px;
  color: #ffffff;
}

.video-item-footer {
  margin-top: 10px;
}

.slot-text {
  display: flex;
  padding-right: 8px;
  flex: 1;
  color: #3b4144;
  flex-direction: column;
  justify-content: space-between;
  overflow: hidden;
}

.slot-text-note {
  display: block;

}
</style>
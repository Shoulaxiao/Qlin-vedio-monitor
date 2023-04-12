<template>
  <view class="all-content">
    <view class="content">
      <calendar :date-current-p.sync="dateCurrent" @change="changeDate"></calendar>

      <view class="data-title">全部事件</view>

      <uni-list :border="true">
        <uni-list-item @click="clickItem(item)" link direction="row" v-for="item in videoList" :key="item.id"
                       :title="item.title"
                       :note="$moment(item.createTime).format('HH:mm')">
          <!-- 通过v-slot:header插槽定义列表左侧的图片显示，其他内容通过List组件内置属性实现-->
          <template v-slot:header>
            <!-- 当前判断长度只为简单判断类型，实际业务中，根据逻辑直接渲染即可 -->
            <image class="image-1" :src="item.preViewImgUrl" mode="aspectFill"></image>
          </template>
        </uni-list-item>
      </uni-list>
    </view>
  </view>
</template>

<script>
import calendar from 'components/calendar/calendar'
import customList from 'components/customlist/custom-list'
import api from '@/api/videoService'
import moment from "moment"

export default {
  components: {calendar, customList},
  name: "index",
  data() {
    return {
      videoList: [],
      dateCurrent: new Date()
    }
  },
  created() {
    this.queryVideoPage()
  },

  methods: {
    async queryVideoPage() {
      let param = {
        pageSize: 10,
        page: 1,
        date: this.dateCurrent
      }
      let res = await api.queryVideosListPage(param)
      if (res.success) {
        this.videoList = res.values
      }else {
        this.videoList = []
      }
    },

    clickItem(item) {
      uni.navigateTo({
        url: '/pages/dynamic/detail?id='+item.id
      })
    },

    changeDate(date) {
      this.dateCurrent = this.$moment(date)
      this.queryVideoPage()
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
</style>
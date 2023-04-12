<template>
  <view class="MgsContent">
    <view class="MsgList">
      <view class="MlSon" v-for="item in dataSource" :key="item.id">
        <view class="MlSonvBox">
          <view class="SonOfImg">
            <image class="Img" :src="item.img"></image>
          </view>
          <view class="SonOfName">
            <view class="SNTop">
              {{ item.title }}
            </view>
            <!--          <view class="SNBom">-->
            <!--            <view class="SBleft">-->
            <!--              左边信息-->
            <!--            </view>-->
            <!--            <view class="SBright">-->
            <!--              右边信息-->
            <!--            </view>-->
            <!--          </view>-->
          </view>
        </view>
      </view>
    </view>
  </view>


</template>

<script>
export default {
  name: "customList",
  props: {
    dataSource: {
      type: Array,
      default: () => []
    },
  },
}
</script>

<style lang="scss" scoped>
$max: 100%;
.MgsContent{
  background-color: #ffffff;
}
.MsgList {
  width: 92%;
  margin-left: 4%;
  max-width: 690px;
  height: auto;

  .MlSon {
    border-radius: 12px;

    .MlSonvBox {
      padding: 24px 32px;
      height: 68px;
      display: flex;
      justify-content: space-around;
      background: #fff;
      //border: 1px solid red;
      margin-top: 10px;

      .SonOfImg {
        //border: 1px solid red;
        width: 164px;
        height: 84px;
        border-radius: 12px;
        overflow: hidden;

        .Img {
          width: $max;
          height: $max;
        }
      }

      .SonOfName {
        width: 428px;
        height: $max;

        .SNTop { //这部分是标题 我将其设置为超出两行隐藏掉您可根据需求设置为一行
          width: $max;
          height: 88px;
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 1;
          line-clamp: 2;
          -webkit-box-orient: vertical;
          line-height: 80px;
          color: #06121E;
          font-size: 14px;
          margin-left: 10px;
        }

        .SNBom {
          width: $max;
          height: 34px;
          line-height: 34px;
          font-size: 24px;
          display: flex;
          justify-content: space-between;
          margin-top: 18px;

          .SBleft { //这里预留了底部左右信息的样式处理
          }

          .SBright {
          }
        }
      }
    }
  }
}

</style>
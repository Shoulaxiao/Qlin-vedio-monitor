<template>
  <view>
    <view class="date-choose shrink">
      <view class="data-month">{{ `${$moment(dateCurrent).format("MM月DD日")} 今天` }}</view>

      <swiper class="date-choose-swiper"
              :indicator-dots="false"
              :current="swiperCurrent"
              @change="dateSwiperChange($event)">
        <block v-if="dateList.length>0" v-for="(date,index) in dateList">
          <swiper-item class="swiper-item">
            <view class="weekday">
              <block v-for="(weekday,ind) in dateListArray">
                <text class="week">{{ weekday }}</text>
              </block>
            </view>
            <view class="dateday">
              <block v-for="day in date.days" :key="day.id">
                <text class="day" :id="day.id" @tap="chooseDate($event)">
                  <text :class="dateCurrentStr===day.id?'active':''">{{ day.day }}</text>
                </text>
              </block>
            </view>
          </swiper-item>
        </block>
      </swiper>
    </view>
  </view>
</template>

<script>

export default {
  name: "calendar",
  props:{
    dateCurrentP:{}
  },
  data() {
    return {
      dateList: [], // 日历数据数组
      swiperCurrent: 0, // 日历轮播正处在哪个索引位置
      dateCurrent: this.dateCurrentP, // 正选择的当前日期
      dateCurrentStr: '', // 正选择日期的 id
      dateMonth: '1月', // 正显示的月份
      dateListArray: ['日', '一', '二', '三', '四', '五', '六']
    }
  },
  created() {
    this.initDate(); // 日历组件程序
  },
  methods: {
    // 日历部分
    // ----------------------------
    initDate() {
      // console.log(this.$util)
      var d = new Date();
      var month = this.$util.addZero(d.getMonth() + 1),
          day = this.$util.addZero(d.getDate());
      for (var i = -5; i <= 5; i++) {
        this.updateDate(this.$util.DateAddDay(d, i * 7)); //多少天之后的
      }
      this.swiperCurrent= 5;
      this.dateCurrent= d;
      this.dateCurrentStr= d.getFullYear() + '-' + month + '-' + day;
      this.dateMonth= month + '月'
    },
    // 获取这周从周日到周六的日期
    calculateDate(_date) {
      var first = this.$util.FirstDayInThisWeek(_date);
      var d = {
        month: first.getMonth() + 1,
        days: []
      };
      for (var i = 0; i < 7; i++) {
        var dd = this.$util.DateAddDay(first, i);
        var day = this.$util.addZero(dd.getDate()),
            month = this.$util.addZero(dd.getMonth() + 1);
        d.days.push({
          day: day,
          id: dd.getFullYear() + '-' + month + '-' + day
        });
      }
      return d;
    },
    // 更新日期数组数据
    updateDate(_date, atBefore) {
      var week = this.calculateDate(_date);
      if (atBefore) {
        this.dateList=[week].concat(this.dateList);
      } else {
        this.dateList=this.dateList.concat(week);
      }
    },
    // 日历组件轮播切换
    dateSwiperChange(e) {
      var index = e.detail.current;
      var d = this.dateList[index];
      this.swiperCurrent=index;
      this.dateMonth= this.$util.addZero(d.month) + '月';
    },
    // 获得日期字符串
    getDateStr: function(arg) {
      if (this.$util.type(arg) == 'array') {
        return arr[0] + '-' + arr[1] + '-' + arr[2] + ' 00:00:00';
      } else if (this.$util.type(arg) == 'date') {
        return arg.getFullYear() + '-' + (arg.getMonth() + 1) + '-' + arg.getDate() + ' 00:00:00';
      } else if (this.$util.type(arg) == 'object') {
        return arg.year + '-' + arg.month + '-' + arg.day + ' 00:00:00';
      }
    },
    // 点击日历某日
    chooseDate(e) {
      var str = e.currentTarget.id;
      this.dateCurrentStr= str
      this.$emit('change',str)
    }
    // ----------------------------
  }
};
</script>

<style lang="scss">
.data-month {
  width: 100%;
  opacity: 0.6;
  align-items: center;
  //padding: 10px 5%;
  font-size: 12px;
  font-weight: bold;
  margin-left: 20px;
  text-align: left;
  /* box-shadow: 2px 0 5px rgba(0,0,0,.4); */
  color: #212020;
}
.date-choose {
  padding: 20px;
  background: #fff;
  overflow: hidden;
  //box-shadow: 0 1px 1px 0 rgba(198, 198, 198, 0.5);

}
.date-choose-swiper {
  flex-grow: 1;
  height: 4em;

}
.swiper-item {
  display: flex;
  flex-direction: column;
}
.weekday,
.dateday {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-align: center;
  flex-grow: 1;

}
.week,
.day {
  width: 14.286%;
  flex-basis: 14.286%;
  padding: 0;
}
.week {
  color: #999;
  font-size: 12px;
}
.day text {
  font-size: 14px;
  font-weight: bold;
  position: relative;
  color: #000303;
}
.day .active:before {
  /* 圈圈 */
  content: '';
  position: absolute;
  width: 30px;
  height: 30px;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  /* border: 2px solid #000; */
  border-radius: 100%;
  background: #dedede;
  z-index: -1;
}
</style>


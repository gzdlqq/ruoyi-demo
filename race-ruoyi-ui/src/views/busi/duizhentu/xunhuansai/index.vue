<template>
  <div class="match-wrap">
    <table class="match-table">
      <tr>
        <!-- 表头 -->
        <td>场次信息</td>
        <td v-for="(col, colIndex) in colList" :key="colIndex">
          {{ '第' + (colIndex+1) + '轮'}}
        </td>
      </tr>

      <!-- 行循环 -->
      <tr v-for="(row, rowIndex) in rowList" :key="rowIndex">
        <!-- 行标题 -->
        <td>{{ '第' + (rowIndex+1) + '场'}}</td>

        <!-- 单元格循环 -->
        <td v-for="(col, colIndex) in colList" :key="colIndex">
          <div class="match-cell">
<!--            获取匹配的场次数据（一个场次包含着两个场次详情）-->
            <span v-for="match in getMatches(row, col)" :key="match.id">
              <span v-if="match.leftOrRight === 1" class="match-player-name">
                {{ match.nickname || '-' }}
              </span>
              <span v-if="match.leftOrRight === 1" class="vs-wrap"><span class="vs">VS</span></span>
              <span v-if="match.leftOrRight === 2" class="match-player-name">
                {{ match.nickname || '-' }}
              </span>
            </span>
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import {xunhuanSessions}  from "@/api/busi/session";

export default {
  name: "MatchTable",
  data() {
    return {
      jsonData: {
      },
      rowList: [],
      colList: [],
      competitionId:null
    };
  },
  methods: {
    generateRowColLists() {
      // Generate row and col lists based on the JSON data
      const row = this.jsonData.row;
      const col = this.jsonData.col;

      this.rowList = Array.from({ length: row }, (_, index) => index + 1);
      this.colList = Array.from({ length: col }, (_, index) => index + 1);

    },
    getMatches(row, col) {
      // debugger
      //this.jsonData.list 获取所有的sessionsDetailList
      const sessionList = this.jsonData.list;
      //定义 存储 对应的两个场次详情
      let sessionDetailList = [];
      //循环matches
      for (let i = 0; i < sessionList.length; i++) {
        let session = sessionList[i];
        if (session.roundSessionNo === row && session.roundNo === col){
          //获取每个session的sessionsDetailList
          sessionDetailList = session.sessionsDetailList;

          return sessionDetailList;
        }
      }
      return sessionDetailList;
    },
    //专门供 父组件调用
    initCompetition(competitionId){
      //先清空jsonData
      this.jsonData = {
      }
      this.rowList=[]
      this.colList=[]

      // debugger
      this.competitionId=competitionId
      //调用后台接口，返回循环赛对阵图接口数据
      xunhuanSessions(competitionId).then(response => {
        this.jsonData = response.data;
        this.generateRowColLists();
      });
    }
  },
  mounted() {

  },
};
</script>
<style>
.match-wrap {
  /*display: flex;*/
  justify-content: center;
  height: 750px;
}
.match-table {
  border-collapse: collapse;
  font-size: 14px;
}
.match-table tr:first-child {
  font-weight: bold;
  text-align: center;
}
.match-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}
.match-table .match-player-name {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: #f5f5f5;
  color: #666;
  min-width: 60px; /* 调整最小宽度，根据实际需要调整 */
}
.match-table .vs {
  color: #999;
}
.match-table .fake {
  color: #bbb;
}
</style>

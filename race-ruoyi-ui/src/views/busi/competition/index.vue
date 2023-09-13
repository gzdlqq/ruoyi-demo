<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="赛事名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入赛事名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报名开始时间" prop="registrationStartTime">
        <el-date-picker clearable
                        v-model="queryParams.registrationStartTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择报名开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="报名结束时间" prop="registrationEndTime">
        <el-date-picker clearable
                        v-model="queryParams.registrationEndTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择报名结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="比赛开始时间" prop="startTime">
        <el-date-picker clearable
                        v-model="queryParams.startTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择比赛开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="预计比赛结束时间" prop="estimatedEndTime">
        <el-date-picker clearable
                        v-model="queryParams.estimatedEndTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择预计比赛结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="赛事是否结束" prop="baseEnd">
        <el-select v-model="queryParams.baseEnd" placeholder="请选择赛事是否结束" clearable>
          <el-option
            v-for="dict in dict.type.busi_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否发布" prop="basePublished">
        <el-select v-model="queryParams.basePublished" placeholder="请选择是否发布" clearable>
          <el-option
            v-for="dict in dict.type.busi_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!--      <el-form-item label="删除标志" prop="delFlag">
              <el-select v-model="queryParams.delFlag" placeholder="请选择删除标志" clearable>
                <el-option
                  v-for="dict in dict.type.busi_yes_no"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['busi:competition:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['busi:competition:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['busi:competition:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['busi:competition:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="competitionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="赛事 ID" align="center" prop="id"/>
      <el-table-column label="赛事名称" align="center" prop="name"/>
<!--      赛事状态 status ，状态 1.即将开始  2.报名中  3.编排中  4.比赛中  5.结束状态 "-->
      <el-table-column label="赛事状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_status" :value="scope.row.status"/>
        </template>
      </el-table-column>

      <el-table-column label="报名开始时间" align="center" prop="registrationStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registrationStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报名结束时间" align="center" prop="registrationEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registrationEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="赛事 banner 图片 URL" align="center" prop="bannerUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.bannerUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="比赛开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预计比赛结束时间" align="center" prop="estimatedEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.estimatedEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="赛事是否结束" align="center" prop="baseEnd">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_yes_no" :value="scope.row.baseEnd"/>
        </template>
      </el-table-column>
      <el-table-column label="赛事地点经度" align="center" prop="longitude"/>
      <el-table-column label="赛事地点纬度" align="center" prop="latitude"/>
      <el-table-column label="是否发布" align="center" prop="basePublished">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_yes_no" :value="scope.row.basePublished"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sort"/>
      <el-table-column label="赛事方案名称" align="center" prop="raceSchema.name"/>
      <el-table-column
        label="赛制"
        width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_format" :value="scope.row.raceSchema.format"/>
        </template>
      </el-table-column>
      <el-table-column label="赛事方案价格" align="center" prop="raceSchema.price"/>
      <el-table-column label="删除标志" align="center" prop="delFlag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_yes_no" :value="scope.row.delFlag"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="155">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="querySignPlayer(scope.row)"
            >查询报名选手信息
            </el-button>


<!--            报完名之后，编排中的状态 才可以进行编排，才显示编排按钮-->
            <!--      赛事状态 status ，状态 1.即将开始  2.报名中  3.编排中  4.比赛中  5.结束状态 "-->

              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="bianpai(scope.row)"
                v-if="scope.row.status == 3"
              >编排
              </el-button>

            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="queryDuizhentu(scope.row)"
            >查看对阵图
            </el-button>



          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="publish(scope.row)"
            v-hasPermi="['busi:competition:xxx']"
          >发布
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="bangdingDialogXX(scope.row)"
            v-hasPermi="['busi:competition:xxx']"
          >绑定赛事方案
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['busi:competition:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['busi:competition:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改赛事对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="赛事名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入赛事名称"/>
        </el-form-item>
        <el-form-item label="报名开始时间" prop="registrationStartTime">
          <el-date-picker clearable
                          v-model="form.registrationStartTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择报名开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报名结束时间" prop="registrationEndTime">
          <el-date-picker clearable
                          v-model="form.registrationEndTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择报名结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="赛事 banner 图片 URL" prop="bannerUrl">
          <image-upload v-model="form.bannerUrl"/>
        </el-form-item>
        <el-form-item label="比赛开始时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择比赛开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预计比赛结束时间" prop="estimatedEndTime">
          <el-date-picker clearable
                          v-model="form.estimatedEndTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择预计比赛结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="赛事是否结束" prop="baseEnd">
          <el-select v-model="form.baseEnd" placeholder="请选择赛事是否结束">
            <el-option
              v-for="dict in dict.type.busi_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="赛事介绍">
          <editor v-model="form.description" :min-height="192"/>
        </el-form-item>
        <el-button @click="mapOpen = true ">地图选点</el-button>
        <el-form-item label="赛事地点经度" prop="longitude">
          <el-input v-model="form.longitude" placeholder="请输入赛事地点经度"/>
        </el-form-item>
        <el-form-item label="赛事地点纬度" prop="latitude">
          <el-input v-model="form.latitude" placeholder="请输入赛事地点纬度"/>
        </el-form-item>
        <!--        <el-form-item label="是否发布" prop="basePublished">-->
        <!--          <el-select v-model="form.basePublished" placeholder="请选择是否发布">-->
        <!--            <el-option-->
        <!--              v-for="dict in dict.type.busi_yes_no"-->
        <!--              :key="dict.value"-->
        <!--              :label="dict.label"-->
        <!--              :value="dict.value"-->
        <!--            ></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序"/>
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-select v-model="form.delFlag" placeholder="请选择删除标志">
            <el-option
              v-for="dict in dict.type.busi_yes_no"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="mapOpen" width="1500px" append-to-body>
      <Map :form="form" v-if="mapOpen" @chageJingweidu="chageJingweidu"></Map>
    </el-dialog>


    <el-dialog title="绑定赛事方案" :visible.sync="openBangdingDialog" width="1500px" @close="closeSaishiFangan" append-to-body>
      <el-table
        ref="singleTable"
        :data="tableData"
        highlight-current-row
        @current-change="handleCurrentChange"
        style="width: 100%">
        <el-table-column
          property="id"
          label="id"
          width="50">
        </el-table-column>
        <el-table-column
          property="name"
          label="方案名称"
          width="120">
        </el-table-column>
        <el-table-column
          label="赛制"
          width="120">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.busi_format" :value="scope.row.format"/>
          </template>
        </el-table-column>
        <el-table-column
          property="price"
          label="价格">
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px">
        <!--        <el-button @click="setCurrent(tableData[1])">选中第二行</el-button>-->
        <!--        <el-button @click="setCurrent()">取消选择</el-button>-->
      </div>
    </el-dialog>


    <div>
      <el-dialog title="对阵图" :visible.sync="xunhuansaiVisible" :modal="true"  width="80%"  :height="800">
        <xunhuansai ref="xunhuansaizizujian"></xunhuansai>
      </el-dialog>
    </div>


  </div>
</template>

<script>
import {
  listCompetition,
  getCompetition,
  delCompetition,
  addCompetition,
  updateCompetition,
  publish
} from "@/api/busi/competition";
import {querySchemaByCompetitionId} from "@/api/busi/schema";
import Map from '@/components/Map/index'
import {arrange} from "@/api/busi/session";
import xunhuansai from '@/views/busi/duizhentu/xunhuansai/index.vue'


export default {
  name: "Competition111",
  components: {
    Map,
    xunhuansai
  },
  dicts: ['busi_yes_no', 'busi_format','busi_status'],
  data() {
    return {
      buzhixingdiyicichaxun: false,
      openBangdingDialog: false,
      currentActiveCompetitonId: null,
      mapOpen: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 赛事表格数据
      competitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        registrationStartTime: null,
        registrationEndTime: null,
        startTime: null,
        estimatedEndTime: null,
        baseEnd: null,
        basePublished: null,
        sort: null,
        delFlag: null,
      },
      // 表单参数
      form: {
        id: null,
        name: null,
        registrationStartTime: null,
        registrationEndTime: null,
        bannerUrl: null,
        startTime: null,
        estimatedEndTime: null,
        baseEnd: null,
        description: null,
        longitude: null,
        latitude: null,
        basePublished: null,
        sort: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      },
      // 表单校验
      rules: {
        name: [
          {required: true, message: "赛事名称不能为空", trigger: "blur"}
        ],
        registrationStartTime: [
          {required: true, message: "报名开始时间不能为空", trigger: "blur"}
        ],
        registrationEndTime: [
          {required: true, message: "报名结束时间不能为空", trigger: "blur"}
        ],
        startTime: [
          {required: true, message: "比赛开始时间不能为空", trigger: "blur"}
        ],
        estimatedEndTime: [
          {required: true, message: "预计比赛结束时间不能为空", trigger: "blur"}
        ],
        baseEnd: [
          {required: true, message: "赛事是否结束不能为空", trigger: "change"}
        ],
        basePublished: [
          {required: true, message: "是否发布不能为空", trigger: "change"}
        ],
        sort: [
          {required: true, message: "排序不能为空", trigger: "blur"}
        ],
      },
      // 赛事方案列表
      tableData: [{
        "createBy": null,
        "createTime": null,
        "updateBy": null,
        "updateTime": null,
        "remark": null,
        "id": null,
        "name": null,
        "format": null,
        "price": null,
        "checked": null
      }],
      currentRow: null,
      xunhuansaiVisible: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //关闭赛事方案弹出层的时候刷新赛事数据
    closeSaishiFangan(){
this.getList()
    },
    //赛事方案的单选
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
    },
    handleCurrentChange(val) {
      if (this.buzhixingdiyicichaxun) {
        this.buzhixingdiyicichaxun = false
        return
      }
      // debugger
      this.currentRow = val;
      console.log(this.currentRow)
      this.currentRow.id
      //调用后台 修改赛事绑定的赛事方案外键的接口即可
      let updatePojo = {
        raceSchemaId: this.currentRow.id,
        id: this.currentActiveCompetitonId
      }
      updateCompetition(updatePojo).then(response => {
        this.$message({
          message: '修改赛事方案成功：',
          type: 'success' // success / warning / info / error
        });
      })
    },
    //打开绑定赛事方案的弹出层
    bangdingDialogXX(row) {
      this.openBangdingDialog = true //弹出层 打开
      //拿着当前赛事的id， 去查询所有赛事方案， 同步需要回显 checked属性
      this.currentActiveCompetitonId = row.id

      querySchemaByCompetitionId(row.id).then(response => {
        console.log(response)
        this.tableData = response.data

        //查询结束之后，需要去遍历 返回的数据，当发现一旦有checked时候，需要拿到对应的索引值，然后调用上面的方法就可以
        const index = this.tableData.findIndex(item => item.checked);
        if (index !== -1) {
          console.log(index)
          this.buzhixingdiyicichaxun = true
          this.$refs.singleTable.setCurrentRow(this.tableData[index]);
        } else {
          this.buzhixingdiyicichaxun = false
        }
      })
    }
    ,
    chageJingweidu($event, address) {
      // debugger
      console.log($event)
      let lat = $event.latLng.lat
      let lng = $event.latLng.lng
      this.form.longitude = lng
      this.form.latitude = lat
      this.form.description = address.address
// debugger
      this.$message({
        message: '选中经纬度：' + lat + "," + lng + ' 地址:' + address.address,
        type: 'success' // success / warning / info / error
      });
    },
    /** 查询赛事列表 */
    getList() {
      this.loading = true;
      listCompetition(this.queryParams).then(response => {
        this.competitionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        registrationStartTime: null,
        registrationEndTime: null,
        bannerUrl: null,
        startTime: null,
        estimatedEndTime: null,
        baseEnd: null,
        description: null,
        longitude: null,
        latitude: null,
        basePublished: null,
        sort: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加赛事";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCompetition(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改赛事";
      });
    },
    publish(row) {
      this.reset();
      const id = row.id || this.ids
      publish(id).then(response => {
        this.$modal.msgSuccess("发布成功");
        this.getList();
      });
    },
    //查询报名选手信息 by 赛事id
    querySignPlayer(row) {
      this.reset();
      const id = row.id || this.ids
      this.$router.push({
        path: '/system/sign',
        query: {
          competitionId: id
        }
      })
    },
    bianpai(row) {
      this.reset();
      const id = row.id || this.ids

      //打开一个loading
      this.$loading({
        lock: true,
        text: '正在编排中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      //调用方法arrange
      arrange(id).then(response => {
        this.$modal.msgSuccess("编排成功");
        // this.getList();
        //关闭loading
        this.$loading().close();
      }).catch(error => {
        this.$modal.msgError("编排失败");
        //关闭loading
        this.$loading().close();

      });

    },
    //查看对阵图 ，弹出层
    queryDuizhentu(row) {
      this.reset();
      const id = row.id || this.ids
      this.xunhuansaiVisible = true //弹出层 打开

      // debugger
/*      //调用子组件xunhuansaizizujian中的initCompetition方法
      let xunhuansaizujian = this.$refs.xunhuansaizizujian;
      // 如果子组件已经被渲染，则直接调用方法
      if (xunhuansaizujian) {
        xunhuansaizujian.initCompetition(id);
      }
      // 如果子组件还没有被渲染，则等待一段时间后再调用方法
      else {
        setTimeout(() => {
          //这个时候 已经渲染完成了，重新 获取标签
          xunhuansaizujian = this.$refs.xunhuansaizizujian;
          xunhuansaizujian.initCompetition(id);
        }, 1000); // 延迟 1 秒钟后再调用
      }*/
      this.$nextTick(() => {
        this.$refs.xunhuansaizizujian.initCompetition(id);
      });

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCompetition(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCompetition(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除赛事编号为"' + ids + '"的数据项？').then(function () {
        return delCompetition(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('busi/competition/export', {
        ...this.queryParams
      }, `competition_${new Date().getTime()}.xlsx`)
    }
  },
  mounted() {
  }
};
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="member fk外键" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入member fk外键"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="选手姓名 冗余" prop="playerName">
        <el-input
          v-model="queryParams.playerName"
          placeholder="请输入选手姓名 冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="赛事id  fk" prop="competitionId">
        <el-input
          v-model="queryParams.competitionId"
          placeholder="请输入赛事id  fk"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="赛事名称 冗余" prop="competitionName">
        <el-input
          v-model="queryParams.competitionName"
          placeholder="请输入赛事名称 冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报名时间" prop="signTime">
        <el-date-picker clearable
                        v-model="queryParams.signTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择报名时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="总金额" prop="totalFee">
        <el-input
          v-model="queryParams.totalFee"
          placeholder="请输入总金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务订单号 全局唯一 一般使用雪花算法" prop="tradeNo">
        <el-input
          v-model="queryParams.tradeNo"
          placeholder="请输入业务订单号 全局唯一 一般使用雪花算法"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="0未付款 1 已经付款(待审核)  2审核通过" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择0未付款 1 已经付款(待审核)  2审核通过" clearable>
          <el-option
            v-for="dict in dict.type.busi_sign_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
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
          v-hasPermi="['busi:sign:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['busi:sign:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['busi:sign:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['busi:sign:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="signList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="member fk外键" align="center" prop="userId" />
      <el-table-column label="选手姓名 冗余" align="center" prop="playerName" />
      <el-table-column label="赛事id  fk" align="center" prop="competitionId" />
      <el-table-column label="赛事名称 冗余" align="center" prop="competitionName" />
      <el-table-column label="报名时间" align="center" prop="signTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" prop="totalFee" />
      <el-table-column label="业务订单号 全局唯一 一般使用雪花算法" align="center" prop="tradeNo" />
      <el-table-column label="0未付款 1 已经付款(待审核)  2审核通过" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.busi_sign_type" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['busi:sign:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['busi:sign:remove']"
          >删除</el-button>
          <template v-if="scope.row.status === '1'">
            <el-button
              size="mini"
              icon="el-icon-check"
              type="success"
              @click="handleAccess(scope.row)"
              v-hasPermi="['busi:sign:allow']"
            >通过</el-button>
            <el-button
              size="mini"
              icon="el-icon-close"
              type="danger"
              @click="handleReject(scope.row)"
              v-hasPermi="['busi:sign:reject']"
            >拒绝</el-button>
          </template>
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

    <!-- 添加或修改sign对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="member fk外键" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入member fk外键" />
        </el-form-item>
        <el-form-item label="选手姓名 冗余" prop="playerName">
          <el-input v-model="form.playerName" placeholder="请输入选手姓名 冗余" />
        </el-form-item>
        <el-form-item label="赛事id  fk" prop="competitionId">
          <el-input v-model="form.competitionId" placeholder="请输入赛事id  fk" />
        </el-form-item>
        <el-form-item label="赛事名称 冗余" prop="competitionName">
          <el-input v-model="form.competitionName" placeholder="请输入赛事名称 冗余" />
        </el-form-item>
        <el-form-item label="报名时间" prop="signTime">
          <el-date-picker clearable
                          v-model="form.signTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择报名时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="总金额" prop="totalFee">
          <el-input v-model="form.totalFee" placeholder="请输入总金额" />
        </el-form-item>
        <el-form-item label="业务订单号 全局唯一 一般使用雪花算法" prop="tradeNo">
          <el-input v-model="form.tradeNo" placeholder="请输入业务订单号 全局唯一 一般使用雪花算法" />
        </el-form-item>
        <el-form-item label="0未付款 1 已经付款(待审核)  2审核通过" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.busi_sign_type"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listSign, getSign, delSign, addSign, updateSign, handleReject,handleAccess} from "@/api/busi/sign";

export default {
  name: "Sign",
  dicts: ['busi_sign_type'],
  data() {
    return {
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
      // sign表格数据
      signList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        playerName: null,
        competitionId: null,
        competitionName: null,
        signTime: null,
        totalFee: null,
        tradeNo: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        playerName: [
          { required: true, message: "选手姓名 冗余不能为空", trigger: "blur" }
        ],
        competitionName: [
          { required: true, message: "赛事名称 冗余不能为空", trigger: "blur" }
        ],
        signTime: [
          { required: true, message: "报名时间不能为空", trigger: "blur" }
        ],
        totalFee: [
          { required: true, message: "总金额不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "0未付款 1 已经付款(待审核)  2审核通过不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    // debugger
    // 不需要执行了，路由守卫中 已经执行了
    // this.getList();
    // alert('初始化了')
  },
  beforeRouteEnter(to, from, next) {
    // 在这个示例中，回调函数 vm => {...} 在组件实例创建后执行，并将组件实例作为 vm 参数传递进去。在回调函数中，你可以通过 vm 访问组件实例的 this 对象，并调用组件的 reset() 方法。
    next(vm => {

      //我希望能够获取 query中传递过来的competitionId的值
      const competitionId = to.query.competitionId; // Accessing competitionId from query parameters

      vm.resetForm("queryForm"); // Calling the reset() method on the Vue component instance

      vm.queryParams.competitionId = competitionId;

      vm.getList();
    });
  },
  methods: {
    /** 查询sign列表 */
    getList() {
      this.loading = true;
      listSign(this.queryParams).then(response => {
        this.signList = response.rows;
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
        userId: null,
        playerName: null,
        competitionId: null,
        competitionName: null,
        signTime: null,
        totalFee: null,
        tradeNo: null,
        status: null
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加sign";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getSign(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改sign";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateSign(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSign(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除sign编号为"' + ids + '"的数据项？').then(function() {
        return delSign(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('busi/sign/export', {
        ...this.queryParams
      }, `sign_${new Date().getTime()}.xlsx`)
    },
    //审核拒绝方法
    async handleReject(row){
      let axiosRes = await handleReject(row.id);
      debugger
      this.$modal.msgSuccess(axiosRes.msg);
      //重新加载列表
      this.getList()
    },
    //审核通过
    async handleAccess(row){
      let axiosRes = await handleAccess(row.id);
      debugger
      this.$modal.msgSuccess(axiosRes.msg);
      //重新加载列表
      this.getList()
    }
  }
};
</script>

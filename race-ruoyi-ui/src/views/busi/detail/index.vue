<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="赛事id FK外键  冗余" prop="competitionId">
        <el-input
          v-model="queryParams.competitionId"
          placeholder="请输入赛事id FK外键  冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总共场次第几场  冗余" prop="sessionNo">
        <el-input
          v-model="queryParams.sessionNo"
          placeholder="请输入总共场次第几场  冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="第几轮  冗余" prop="roundNo">
        <el-input
          v-model="queryParams.roundNo"
          placeholder="请输入第几轮  冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轮内第几场  冗余" prop="roundSessionNo">
        <el-input
          v-model="queryParams.roundSessionNo"
          placeholder="请输入轮内第几场  冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轮内位置号" prop="locationNo">
        <el-input
          v-model="queryParams.locationNo"
          placeholder="请输入轮内位置号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下一轮的位置号" prop="nextRoundLocationNo">
        <el-input
          v-model="queryParams.nextRoundLocationNo"
          placeholder="请输入下一轮的位置号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报名id" prop="signId">
        <el-input
          v-model="queryParams.signId"
          placeholder="请输入报名id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户姓名，冗余" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入用户姓名，冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="场次是否结束 1结束 0未结束  冗余" prop="baseGameOver">
        <el-input
          v-model="queryParams.baseGameOver"
          placeholder="请输入场次是否结束 1结束 0未结束  冗余"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="1获胜 2失败" prop="result">
        <el-input
          v-model="queryParams.result"
          placeholder="请输入1获胜 2失败"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['busi:detail:add']"
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
          v-hasPermi="['busi:detail:edit']"
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
          v-hasPermi="['busi:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['busi:detail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="赛事id FK外键  冗余" align="center" prop="competitionId" />
      <el-table-column label="总共场次第几场  冗余" align="center" prop="sessionNo" />
      <el-table-column label="第几轮  冗余" align="center" prop="roundNo" />
      <el-table-column label="轮内第几场  冗余" align="center" prop="roundSessionNo" />
      <el-table-column label="轮内位置号" align="center" prop="locationNo" />
      <el-table-column label="下一轮的位置号" align="center" prop="nextRoundLocationNo" />
      <el-table-column label="报名id" align="center" prop="signId" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="用户姓名，冗余" align="center" prop="nickname" />
      <el-table-column label="场次是否结束 1结束 0未结束  冗余" align="center" prop="baseGameOver" />
      <el-table-column label="1获胜 2失败" align="center" prop="result" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['busi:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['busi:detail:remove']"
          >删除</el-button>
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

    <!-- 添加或修改场次详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="赛事id FK外键  冗余" prop="competitionId">
          <el-input v-model="form.competitionId" placeholder="请输入赛事id FK外键  冗余" />
        </el-form-item>
        <el-form-item label="总共场次第几场  冗余" prop="sessionNo">
          <el-input v-model="form.sessionNo" placeholder="请输入总共场次第几场  冗余" />
        </el-form-item>
        <el-form-item label="第几轮  冗余" prop="roundNo">
          <el-input v-model="form.roundNo" placeholder="请输入第几轮  冗余" />
        </el-form-item>
        <el-form-item label="轮内第几场  冗余" prop="roundSessionNo">
          <el-input v-model="form.roundSessionNo" placeholder="请输入轮内第几场  冗余" />
        </el-form-item>
        <el-form-item label="轮内位置号" prop="locationNo">
          <el-input v-model="form.locationNo" placeholder="请输入轮内位置号" />
        </el-form-item>
        <el-form-item label="下一轮的位置号" prop="nextRoundLocationNo">
          <el-input v-model="form.nextRoundLocationNo" placeholder="请输入下一轮的位置号" />
        </el-form-item>
        <el-form-item label="报名id" prop="signId">
          <el-input v-model="form.signId" placeholder="请输入报名id" />
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="用户姓名，冗余" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入用户姓名，冗余" />
        </el-form-item>
        <el-form-item label="场次是否结束 1结束 0未结束  冗余" prop="baseGameOver">
          <el-input v-model="form.baseGameOver" placeholder="请输入场次是否结束 1结束 0未结束  冗余" />
        </el-form-item>
        <el-form-item label="1获胜 2失败" prop="result">
          <el-input v-model="form.result" placeholder="请输入1获胜 2失败" />
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
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/busi/detail";

export default {
  name: "Detail",
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
      // 场次详情表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        competitionId: null,
        sessionNo: null,
        roundNo: null,
        roundSessionNo: null,
        locationNo: null,
        nextRoundLocationNo: null,
        signId: null,
        userId: null,
        nickname: null,
        baseGameOver: null,
        result: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sessionNo: [
          { required: true, message: "总共场次第几场  冗余不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询场次详情列表 */
    getList() {
      this.loading = true;
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows;
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
        competitionId: null,
        sessionNo: null,
        roundNo: null,
        roundSessionNo: null,
        locationNo: null,
        nextRoundLocationNo: null,
        signId: null,
        userId: null,
        nickname: null,
        baseGameOver: null,
        result: null
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
      this.title = "添加场次详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改场次详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除场次详情编号为"' + ids + '"的数据项？').then(function() {
        return delDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('busi/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

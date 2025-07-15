<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="140px">
      <el-form-item label="无人机名称" prop="vehicleName">
        <el-input v-model="queryParams.vehicleName" placeholder="请输入无人机名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="无人机ip地址" prop="vehicleIp">
        <el-input v-model="queryParams.vehicleIp" placeholder="请输入无人机ip地址" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="无人机所属组ID" prop="vehicleGroupId">
        <el-input v-model="queryParams.vehicleGroupId" placeholder="请输入无人机所属组ID" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="无人机所属组名" prop="vehicleGroupName">
        <el-input v-model="queryParams.vehicleGroupName" placeholder="请输入无人机所属组名" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="无人机系统ID" prop="sysId">
        <el-input v-model="queryParams.sysId" placeholder="请输入无人机系统ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:vehicle:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:vehicle:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:vehicle:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:vehicle:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="无人机id" align="center" width="80" prop="vehicleId" />
      <el-table-column label="无人机名称" align="center" width="100" prop="vehicleName" :show-overflow-tooltip="true" />
      <el-table-column label="无人机ip地址" align="center" width="120" prop="vehicleIp" />
      <el-table-column label="无人机port端口" align="center" width="120" prop="vehiclePort" />
      <el-table-column label="视频流地址" align="center" width="120" prop="vehicleRtsp" show-overflow-tooltip/>
      <el-table-column label="所属组ID" align="center" width="120" prop="vehicleGroupId" />
      <el-table-column label="所属组名" align="center" width="120" prop="vehicleGroupName" />
      <el-table-column label="在线状态" align="center" width="120" prop="vehicleOnlineStatus" />
      <el-table-column label="无人机状态:" align="center" width="120" prop="vehicleStatus" />
      <el-table-column label="无人机系统ID" align="center" width="120" prop="sysId" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:vehicle:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:vehicle:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改无人机基础信息管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="无人机名称" prop="vehicleName">
          <el-input v-model="form.vehicleName" placeholder="请输入无人机名称" />
        </el-form-item>
        <el-form-item label="无人机ip地址" prop="vehicleIp">
          <el-input v-model="form.vehicleIp" placeholder="请输入无人机ip地址" />
        </el-form-item>
        <el-form-item label="无人机port端口" prop="vehiclePort">
          <el-input v-model="form.vehiclePort" placeholder="请输入无人机port端口" />
        </el-form-item>
        <el-form-item label="无人机视频流地址" prop="vehicleRtsp">
          <el-input v-model="form.vehicleRtsp" placeholder="请输入无人机视频流地址" />
        </el-form-item>
        <el-form-item label="无人机所属组ID" prop="vehicleGroupId">
          <el-input v-model="form.vehicleGroupId" placeholder="请输入无人机所属组ID" />
        </el-form-item>
        <el-form-item label="无人机所属组名" prop="vehicleGroupName">
          <el-input v-model="form.vehicleGroupName" placeholder="请输入无人机所属组名" />
        </el-form-item>
        <el-form-item label="无人机系统ID" prop="sysId">
          <el-input v-model="form.sysId" placeholder="请输入无人机系统ID" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listVehicle, getVehicle, delVehicle, addVehicle, updateVehicle } from "@/api/vehicle/vehicle";

export default {
  name: "Vehicle",
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
      // 无人机基础信息管理表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleName: null,
        vehicleIp: null,
        vehiclePort: null,
        vehicleRtsp: null,
        vehicleGroupId: null,
        vehicleGroupName: null,
        vehicleOnlineStatus: null,
        vehicleStatus: null,
        sysId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehicleStatus: [
          { required: true, message: "无人机状态: 0：故障，1：正常不能为空", trigger: "change" }
        ],
        sysId: [
          { required: true, message: "无人机系统ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询无人机基础信息管理列表 */
    getList() {
      this.loading = true;
      listVehicle(this.queryParams).then(response => {
        this.vehicleList = response.rows;
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
        vehicleId: null,
        vehicleName: null,
        vehicleIp: null,
        vehiclePort: null,
        vehicleRtsp: null,
        vehicleGroupId: null,
        vehicleGroupName: null,
        vehicleOnlineStatus: null,
        vehicleStatus: null,
        sysId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.vehicleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加无人机基础信息管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const vehicleId = row.vehicleId || this.ids
      getVehicle(vehicleId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改无人机基础信息管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.vehicleId != null) {
            updateVehicle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVehicle(this.form).then(response => {
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
      const vehicleIds = row.vehicleId || this.ids;
      this.$modal.confirm('是否确认删除无人机基础信息管理编号为"' + vehicleIds + '"的数据项？').then(function () {
        return delVehicle(vehicleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/vehicle/export', {
        ...this.queryParams
      }, `vehicle_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

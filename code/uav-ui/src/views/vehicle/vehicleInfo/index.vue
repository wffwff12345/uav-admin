<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="无人机ID" prop="vehicleId">
        <el-input v-model="queryParams.vehicleId" placeholder="请输入无人机ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="无人机系统ID" prop="sysId">
        <el-input v-model="queryParams.sysId" placeholder="请输入无人机系统ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="飞行模式" prop="mode">
        <el-input v-model="queryParams.mode" placeholder="请输入飞行模式" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:info:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="vehicleInfoId" />
      <el-table-column label="无人机ID" align="center" prop="vehicleId" />
      <el-table-column label="无人机系统ID" align="center" width="120" prop="sysId" />
      <el-table-column label="地速度 km/h" align="center" width="120" prop="groundSpeed" />
      <el-table-column label="电量百分比" align="center" width="120" prop="soc" />
      <el-table-column label="无人机经度" align="center" width="120" prop="lng" show-overflow-tooltip />
      <el-table-column label="无人机纬度" align="center" width="120" prop="lat" show-overflow-tooltip />
      <el-table-column label="无人机海拔高度" align="center" width="120" prop="alt" />
      <el-table-column label="无人机飞行时间" align="center" width="120" prop="flyInAir" />
      <el-table-column label="飞机航向" align="center" prop="headAngle" show-overflow-tooltip />
      <el-table-column label="gps状态" align="center" prop="gpsStatus" />
      <el-table-column label="卫星数" align="center" prop="satelliteCount" />
      <el-table-column label="飞行模式" align="center" prop="mode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:info:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:info:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改无人机实时信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="无人机ID" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入无人机ID" />
        </el-form-item>
        <el-form-item label="无人机系统ID" prop="sysId">
          <el-input v-model="form.sysId" placeholder="请输入无人机系统ID" />
        </el-form-item>
        <el-form-item label="无人机地速度 km/h" prop="groundSpeed">
          <el-input v-model="form.groundSpeed" placeholder="请输入无人机地速度 km/h" />
        </el-form-item>
        <el-form-item label="无人机电量百分比" prop="soc">
          <el-input v-model="form.soc" placeholder="请输入无人机电量百分比" />
        </el-form-item>
        <el-form-item label="无人机经度" prop="lng">
          <el-input v-model="form.lng" placeholder="请输入无人机经度" />
        </el-form-item>
        <el-form-item label="无人机纬度" prop="lat">
          <el-input v-model="form.lat" placeholder="请输入无人机纬度" />
        </el-form-item>
        <el-form-item label="无人机海拔高度" prop="alt">
          <el-input v-model="form.alt" placeholder="请输入无人机海拔高度" />
        </el-form-item>
        <el-form-item label="无人机飞行时间" prop="flyInAir">
          <el-input v-model="form.flyInAir" placeholder="请输入无人机飞行时间" />
        </el-form-item>
        <el-form-item label="飞机航向" prop="headAngle">
          <el-input v-model="form.headAngle" placeholder="请输入飞机航向" />
        </el-form-item>
        <el-form-item label="卫星数" prop="satelliteCount">
          <el-input v-model="form.satelliteCount" placeholder="请输入卫星数" />
        </el-form-item>
        <el-form-item label="飞行模式" prop="mode">
          <el-input v-model="form.mode" placeholder="请输入飞行模式" />
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo } from "@/api/vehicle/uavVehicleInfo";

export default {
  name: "Info",
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
      // 无人机实时信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleId: null,
        sysId: null,
        groundSpeed: null,
        soc: null,
        lng: null,
        lat: null,
        alt: null,
        flyInAir: null,
        headAngle: null,
        gpsStatus: null,
        satelliteCount: null,
        mode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sysId: [
          { required: true, message: "无人机系统ID不能为空", trigger: "blur" }
        ],
        groundSpeed: [
          { required: true, message: "无人机地速度 km/h不能为空", trigger: "blur" }
        ],
        soc: [
          { required: true, message: "无人机电量百分比不能为空", trigger: "blur" }
        ],
        lng: [
          { required: true, message: "无人机经度不能为空", trigger: "blur" }
        ],
        lat: [
          { required: true, message: "无人机纬度不能为空", trigger: "blur" }
        ],
        alt: [
          { required: true, message: "无人机海拔高度不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询无人机实时信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
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
        vehicleInfoId: null,
        vehicleId: null,
        sysId: null,
        groundSpeed: null,
        soc: null,
        lng: null,
        lat: null,
        alt: null,
        flyInAir: null,
        headAngle: null,
        gpsStatus: null,
        satelliteCount: null,
        mode: null,
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
      this.ids = selection.map(item => item.vehicleInfoId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加无人机实时信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const vehicleInfoId = row.vehicleInfoId || this.ids
      getInfo(vehicleInfoId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改无人机实时信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.vehicleInfoId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
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
      const vehicleInfoIds = row.vehicleInfoId || this.ids;
      this.$modal.confirm('是否确认删除无人机实时信息编号为"' + vehicleInfoIds + '"的数据项？').then(function () {
        return delInfo(vehicleInfoIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

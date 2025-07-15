package com.ruoyi.uav.admin.controller;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.websocket.domain.VehicleInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.uav.admin.service.IUavVehicleInfoService;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 无人机实时状态信息管理Controller
 * 
 * @author ruoyi
 * @date 2024-09-03
 */
@RestController
@RequestMapping("/admin/uavVehicleInfo")
public class UavVehicleInfoController extends BaseController
{
    @Autowired
    private IUavVehicleInfoService uavVehicleInfoService;

    /**
     * 查询无人机基础信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('admin:uavVehicleInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(VehicleInfo vehicleInfo)
    {
        startPage();
        List<VehicleInfo> list = uavVehicleInfoService.selectUavVehicleInfoList(vehicleInfo);
        return getDataTable(list);
    }

    /**
     * 导出无人机基础信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('admin:uavVehicleInfo:export')")
    @Log(title = "无人机基础信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VehicleInfo vehicleInfo)
    {
        List<VehicleInfo> list = uavVehicleInfoService.selectUavVehicleInfoList(vehicleInfo);
        ExcelUtil<VehicleInfo> util = new ExcelUtil<VehicleInfo>(VehicleInfo.class);
        util.exportExcel(response, list, "无人机基础信息管理数据");
    }

    /**
     * 获取无人机基础信息管理详细信息
     */
    @GetMapping(value = "/{vehicleId}")
    public AjaxResult getInfo(@PathVariable("vehicleId") Long vehicleId)
    {
        return success(uavVehicleInfoService.selectUavVehicleInfoByVehicleId(vehicleId));
    }

    /**
     * 新增无人机基础信息管理
     */
    @PreAuthorize("@ss.hasPermi('admin:uavVehicleInfo:add')")
    @Log(title = "无人机基础信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VehicleInfo vehicleInfo)
    {
        return toAjax(uavVehicleInfoService.insertUavVehicleInfo(vehicleInfo));
    }

    /**
     * 修改无人机基础信息管理
     */
    @PreAuthorize("@ss.hasPermi('admin:uavVehicleInfo:edit')")
    @Log(title = "无人机基础信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VehicleInfo vehicleInfo)
    {
        return toAjax(uavVehicleInfoService.updateUavVehicleInfo(vehicleInfo));
    }

    /**
     * 删除无人机基础信息管理
     */
    @PreAuthorize("@ss.hasPermi('admin:uavVehicleInfo:remove')")
    @Log(title = "无人机基础信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vehicleIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleIds)
    {
        return toAjax(uavVehicleInfoService.deleteUavVehicleInfoByVehicleIds(vehicleIds));
    }

    // 连接无人机
	/*@GetMapping("/new/start/{vehicleId}")
    public AjaxResult startConnectVehicle(@PathVariable("vehicleId") Long vehicleId)
    {
        return toAjax(uavVehicleInfoService.startConnectVehicle(vehicleId));
    }*/

    // 断开无人机
	/*@GetMapping("/new/stop/{vehicleId}")
    public AjaxResult stopConnectVehicle(@PathVariable("vehicleId") Long vehicleId)
    {
        return toAjax(uavVehicleInfoService.stopConnectVehicle(vehicleId));
    }*/

    // 获取连接的无人机列表
   /* @GetMapping("/new/connect/list")
    public TableDataInfo getConnectVehicleList()
    {
        Vehicle vehicle = new Vehicle();
        startPage();
        List<Vehicle> list = uavVehicleInfoService.selectUavVehicleInfoList(vehicle);
        return getDataTable(list);
    }*/
}

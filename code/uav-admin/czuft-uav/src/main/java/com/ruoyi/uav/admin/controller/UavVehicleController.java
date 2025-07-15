package com.ruoyi.uav.admin.controller;
import javax.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.uav.admin.service.IUavVehicleService;
import com.ruoyi.websocket.domain.Vehicle;
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
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 无人机信息Controller
 *
 * @author ruoyi
 * @date 2024-09-02
 */
@RestController
@RequestMapping("/system/vehicle")
public class UavVehicleController extends BaseController {
    @Autowired
    private IUavVehicleService uavVehicleService;

    /**
     * 查询无人机信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Vehicle vehicle) {
        startPage();
        List<Vehicle> list = uavVehicleService.selectUavVehicleList(vehicle);
        return getDataTable(list);
    }

    /**
     * 导出无人机信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:export')")
    @Log(title = "无人机信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Vehicle vehicle) {
        List<Vehicle> list = uavVehicleService.selectUavVehicleList(vehicle);
        ExcelUtil<Vehicle> util = new ExcelUtil<Vehicle>(Vehicle.class);
        util.exportExcel(response, list, "无人机信息数据");
    }

    /**
     * 获取无人机信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:query')")
    @GetMapping(value = "/{vehicleId}")
    public AjaxResult getInfo(@PathVariable("vehicleId") Long vehicleId) {
        return success(uavVehicleService.selectUavVehicleByVehicleId(vehicleId));
    }

    /**
     * 新增无人机信息
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:add')")
    @Log(title = "无人机信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Vehicle vehicle) {
        return toAjax(uavVehicleService.insertUavVehicle(vehicle));
    }

    /**
     * 修改无人机信息
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:edit')")
    @Log(title = "无人机信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Vehicle vehicle) {
        return toAjax(uavVehicleService.updateUavVehicle(vehicle));
    }

    /**
     * 删除无人机信息
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:remove')")
    @Log(title = "无人机信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{vehicleIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleIds) {
        return toAjax(uavVehicleService.deleteUavVehicleByVehicleIds(vehicleIds));
    }

//    @PostMapping("/locus")
//    public AjaxResult getVehicleLocus(@RequestBody UavVehicle vehicle) {
//        UavVehicle uavVehicle = new UavVehicle();
//        uavVehicle.setMac(vehicleMac);
//        return success(uavVehicleService.selectUavVehicleList(uavVehicle));
//    }
}

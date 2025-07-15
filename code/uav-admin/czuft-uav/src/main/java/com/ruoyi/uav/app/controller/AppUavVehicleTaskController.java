package com.ruoyi.uav.app.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.uav.admin.service.IUavVehicleTaskService;
import com.ruoyi.websocket.domain.UavVehicleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 无人机任务信息Controller
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
@RestController
@RequestMapping("/app/task")
public class AppUavVehicleTaskController extends BaseController
{
    @Autowired
    private IUavVehicleTaskService uavVehicleTaskService;

    /**
     * 查询无人机任务信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UavVehicleTask uavVehicleTask)
    {
        startPage();
        List<UavVehicleTask> list = uavVehicleTaskService.selectUavVehicleTaskList(uavVehicleTask);
        return getDataTable(list);
    }

    /**
     * 导出无人机任务信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:task:export')")
    @Log(title = "无人机任务信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UavVehicleTask uavVehicleTask)
    {
        List<UavVehicleTask> list = uavVehicleTaskService.selectUavVehicleTaskList(uavVehicleTask);
        ExcelUtil<UavVehicleTask> util = new ExcelUtil<UavVehicleTask>(UavVehicleTask.class);
        util.exportExcel(response, list, "无人机任务信息数据");
    }

    /**
     * 获取无人机任务信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:task:query')")
    @GetMapping(value = "/{vehicleTaskId}")
    public AjaxResult getInfo(@PathVariable("vehicleTaskId") Long vehicleTaskId)
    {
        return success(uavVehicleTaskService.selectUavVehicleTaskByVehicleTaskId(vehicleTaskId));
    }

    /**
     * 新增无人机任务信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody UavVehicleTask uavVehicleTask)
    {
        System.out.println("uavVehicleTask  uavVehicleTask"+uavVehicleTask.toString());
        return success(uavVehicleTaskService.insertUavVehicleTask(uavVehicleTask));
    }

    /**
     * 修改无人机任务信息
     */
    @PreAuthorize("@ss.hasPermi('system:task:edit')")
    @Log(title = "无人机任务信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UavVehicleTask uavVehicleTask)
    {
        return toAjax(uavVehicleTaskService.updateUavVehicleTask(uavVehicleTask));
    }

    /**
     * 删除无人机任务信息
     */
    @PreAuthorize("@ss.hasPermi('system:task:remove')")
    @Log(title = "无人机任务信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vehicleTaskIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleTaskIds)
    {
        return toAjax(uavVehicleTaskService.deleteUavVehicleTaskByVehicleTaskIds(vehicleTaskIds));
    }
}

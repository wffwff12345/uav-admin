package com.ruoyi.uav.admin.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.uav.admin.domain.UavVehicleTaskItem;
import com.ruoyi.uav.admin.service.IUavVehicleTaskItemService;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 无人机任务项信息Controller
 * 
 * @author ruoyi
 * @date 2025-02-07
 */
@RestController
@RequestMapping("/admin/task/item")
public class UavVehicleTaskItemController extends BaseController
{
    @Autowired
    private IUavVehicleTaskItemService uavVehicleTaskItemService;

    /**
     * 查询无人机任务项信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(UavVehicleTaskItem uavVehicleTaskItem)
    {
        startPage();
        List<UavVehicleTaskItem> list = uavVehicleTaskItemService.selectUavVehicleTaskItemList(uavVehicleTaskItem);
        return getDataTable(list);
    }

    /**
     * 导出无人机任务项信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:item:export')")
    @Log(title = "无人机任务项信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UavVehicleTaskItem uavVehicleTaskItem)
    {
        List<UavVehicleTaskItem> list = uavVehicleTaskItemService.selectUavVehicleTaskItemList(uavVehicleTaskItem);
        ExcelUtil<UavVehicleTaskItem> util = new ExcelUtil<UavVehicleTaskItem>(UavVehicleTaskItem.class);
        util.exportExcel(response, list, "无人机任务项信息数据");
    }

    /**
     * 获取无人机任务项信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:item:query')")
    @GetMapping(value = "/{vehicleTaskItemId}")
    public AjaxResult getInfo(@PathVariable("vehicleTaskItemId") Long vehicleTaskItemId)
    {
        return success(uavVehicleTaskItemService.selectUavVehicleTaskItemByVehicleTaskItemId(vehicleTaskItemId));
    }

    /**
     * 新增无人机任务项信息
     */
    @PreAuthorize("@ss.hasPermi('system:item:add')")
    @Log(title = "无人机任务项信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UavVehicleTaskItem uavVehicleTaskItem)
    {
        return toAjax(uavVehicleTaskItemService.insertUavVehicleTaskItem(uavVehicleTaskItem));
    }

    /**
     * 修改无人机任务项信息
     */
    @PreAuthorize("@ss.hasPermi('system:item:edit')")
    @Log(title = "无人机任务项信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UavVehicleTaskItem uavVehicleTaskItem)
    {
        return toAjax(uavVehicleTaskItemService.updateUavVehicleTaskItem(uavVehicleTaskItem));
    }

    /**
     * 删除无人机任务项信息
     */
    @PreAuthorize("@ss.hasPermi('system:item:remove')")
    @Log(title = "无人机任务项信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{vehicleTaskItemIds}")
    public AjaxResult remove(@PathVariable Long[] vehicleTaskItemIds)
    {
        return toAjax(uavVehicleTaskItemService.deleteUavVehicleTaskItemByVehicleTaskItemIds(vehicleTaskItemIds));
    }
}

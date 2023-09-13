package com.ruoyi.web.controller.busi.admin;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.busi.domain.TSessionDetail;
import com.ruoyi.busi.service.ITSessionDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 场次详情Controller
 *
 * @author shenzhao
 * @date 2023-05-07
 */
@RestController
@RequestMapping("/busi/detail")
public class TSessionDetailController extends BaseController
{
    @Autowired
    private ITSessionDetailService tSessionDetailService;

    /**
     * 查询场次详情列表
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSessionDetail tSessionDetail)
    {
        startPage();
        List<TSessionDetail> list = tSessionDetailService.selectTSessionDetailList(tSessionDetail);
        return getDataTable(list);
    }

    /**
     * 导出场次详情列表
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:export')")
    @Log(title = "场次详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSessionDetail tSessionDetail)
    {
        List<TSessionDetail> list = tSessionDetailService.selectTSessionDetailList(tSessionDetail);
        ExcelUtil<TSessionDetail> util = new ExcelUtil<TSessionDetail>(TSessionDetail.class);
        util.exportExcel(response, list, "场次详情数据");
    }

    /**
     * 获取场次详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tSessionDetailService.selectTSessionDetailById(id));
    }

    /**
     * 新增场次详情
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:add')")
    @Log(title = "场次详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSessionDetail tSessionDetail)
    {
        return toAjax(tSessionDetailService.insertTSessionDetail(tSessionDetail));
    }

    /**
     * 修改场次详情
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:edit')")
    @Log(title = "场次详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSessionDetail tSessionDetail)
    {
        return toAjax(tSessionDetailService.updateTSessionDetail(tSessionDetail));
    }

    /**
     * 删除场次详情
     */
    @PreAuthorize("@ss.hasPermi('busi:detail:remove')")
    @Log(title = "场次详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSessionDetailService.deleteTSessionDetailByIds(ids));
    }
}

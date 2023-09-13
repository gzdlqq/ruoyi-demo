package com.ruoyi.web.controller.busi.admin;

import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.busi.service.ITCompetitionService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 赛事Controller
 *
 * @author shenzhao
 * @date 2023-04-03
 */
@RestController
@RequestMapping("/busi/competition")
public class TCompetitionController extends BaseController
{
    @Autowired
    private ITCompetitionService tCompetitionService;

    /**
     * 查询赛事列表
     */
//    @RepeatSubmit
    @PreAuthorize("@ss.hasPermi('busi:competition:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCompetition tCompetition)
    {
        startPage();
        List<TCompetition> list = tCompetitionService.selectTCompetitionList(tCompetition);
        return getDataTable(list);
    }

    /**
     * 导出赛事列表
     */
    @PreAuthorize("@ss.hasPermi('busi:competition:export')")
    @Log(title = "赛事", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCompetition tCompetition)
    {
        List<TCompetition> list = tCompetitionService.selectTCompetitionList(tCompetition);
        ExcelUtil<TCompetition> util = new ExcelUtil<TCompetition>(TCompetition.class);
        util.exportExcel(response, list, "赛事数据");
    }

    /**
     * 获取赛事详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:competition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCompetitionService.selectTCompetitionById(id));
    }

    /**
     * 发布
     */
    @GetMapping(value = "/publish/{id}")
    public AjaxResult publish(@PathVariable("id") Long id)
    {
        tCompetitionService.publish(id);
        return success("发布成功");
    }

    /**
     * 新增赛事
     */
    @PreAuthorize("@ss.hasPermi('busi:competition:add')")
    @Log(title = "赛事", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCompetition tCompetition)
    {
        return toAjax(tCompetitionService.insertTCompetition(tCompetition));
    }

    /**
     * 修改赛事
     */
    @PreAuthorize("@ss.hasPermi('busi:competition:edit')")
    @Log(title = "赛事", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCompetition tCompetition)
    {
        return toAjax(tCompetitionService.updateTCompetition(tCompetition));
    }

    /**
     * 删除赛事
     */
    @PreAuthorize("@ss.hasPermi('busi:competition:remove')")
    @Log(title = "赛事", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCompetitionService.deleteTCompetitionByIds(ids));
    }
}

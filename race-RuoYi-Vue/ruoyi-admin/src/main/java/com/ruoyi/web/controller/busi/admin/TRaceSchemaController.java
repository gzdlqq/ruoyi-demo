package com.ruoyi.web.controller.busi.admin;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.busi.domain.resp.TRaceSchemaResp;
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
import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.busi.service.ITRaceSchemaService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 赛事方案Controller
 *
 * @author shenzhao
 * @date 2023-04-05
 */
@RestController
@RequestMapping("/busi/schema")
public class TRaceSchemaController extends BaseController
{
    @Autowired
    private ITRaceSchemaService tRaceSchemaService;

    /**
     * 查询所有赛事方案，同时 回显 是否当前赛事已经选中
     */
    @GetMapping(value = "/querySchemaByCompetitionId/{id}")
    public AjaxResult querySchemaByCompetitionId(@PathVariable("id") Long id)
    {
       List<TRaceSchemaResp> list = tRaceSchemaService.querySchemaByCompetitionId(id);
        return success(list);
    }

    /**
     * 查询赛事方案列表
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:list')")
    @GetMapping("/list")
    public TableDataInfo list(TRaceSchema tRaceSchema)
    {
        startPage();
        List<TRaceSchema> list = tRaceSchemaService.selectTRaceSchemaList(tRaceSchema);
        return getDataTable(list);
    }

    /**
     * 导出赛事方案列表
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:export')")
    @Log(title = "赛事方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRaceSchema tRaceSchema)
    {
        List<TRaceSchema> list = tRaceSchemaService.selectTRaceSchemaList(tRaceSchema);
        ExcelUtil<TRaceSchema> util = new ExcelUtil<TRaceSchema>(TRaceSchema.class);
        util.exportExcel(response, list, "赛事方案数据");
    }

    /**
     * 获取赛事方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tRaceSchemaService.selectTRaceSchemaById(id));
    }

    /**
     * 新增赛事方案
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:add')")
    @Log(title = "赛事方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRaceSchema tRaceSchema)
    {
        return toAjax(tRaceSchemaService.insertTRaceSchema(tRaceSchema));
    }

    /**
     * 修改赛事方案
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:edit')")
    @Log(title = "赛事方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRaceSchema tRaceSchema)
    {
        return toAjax(tRaceSchemaService.updateTRaceSchema(tRaceSchema));
    }

    /**
     * 删除赛事方案
     */
    @PreAuthorize("@ss.hasPermi('busi:schema:remove')")
    @Log(title = "赛事方案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tRaceSchemaService.deleteTRaceSchemaByIds(ids));
    }
}

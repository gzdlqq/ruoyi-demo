package com.ruoyi.web.controller.busi.admin;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.busi.domain.TSession;
import com.ruoyi.busi.service.ITSessionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 场次Controller
 *
 * @author shenzhao
 * @date 2023-05-07
 */
@Api(tags = "场次相关接口")
@RestController
@RequestMapping("/busi/session")
public class TSessionController extends BaseController
{
    @Autowired
    private ITSessionService tSessionService;


    @GetMapping("/xunhuanSessions")
    @ApiOperation("循环赛场次查询")
    @Anonymous
    public AjaxResult xunhuanSessions(Long competitionId)
    {
        return success(tSessionService.xunhuanSessions(competitionId));
    }

    /**
     * 场次编排接口
     */
    @GetMapping("/arrange")
    @ApiOperation("根据赛事id进行编排")
    @Anonymous
    public AjaxResult arrange(Long competitionId)
    {
        return success(tSessionService.arrange(competitionId));
    }


    /**
     * 查询场次列表
     */
    @PreAuthorize("@ss.hasPermi('busi:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSession tSession)
    {
        startPage();
        List<TSession> list = tSessionService.selectTSessionList(tSession);
        return getDataTable(list);
    }

    /**
     * 导出场次列表
     */
    @PreAuthorize("@ss.hasPermi('busi:session:export')")
    @Log(title = "场次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSession tSession)
    {
        List<TSession> list = tSessionService.selectTSessionList(tSession);
        ExcelUtil<TSession> util = new ExcelUtil<TSession>(TSession.class);
        util.exportExcel(response, list, "场次数据");
    }

    /**
     * 获取场次详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:session:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tSessionService.selectTSessionById(id));
    }

    /**
     * 新增场次
     */
    @PreAuthorize("@ss.hasPermi('busi:session:add')")
    @Log(title = "场次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSession tSession)
    {
        return toAjax(tSessionService.insertTSession(tSession));
    }

    /**
     * 修改场次
     */
    @PreAuthorize("@ss.hasPermi('busi:session:edit')")
    @Log(title = "场次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSession tSession)
    {
        return toAjax(tSessionService.updateTSession(tSession));
    }

    /**
     * 删除场次
     */
    @PreAuthorize("@ss.hasPermi('busi:session:remove')")
    @Log(title = "场次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSessionService.deleteTSessionByIds(ids));
    }
}

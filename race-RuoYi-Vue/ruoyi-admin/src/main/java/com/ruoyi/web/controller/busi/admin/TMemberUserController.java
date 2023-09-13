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
import com.ruoyi.busi.domain.TMemberUser;
import com.ruoyi.busi.service.ITMemberUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户Controller
 *
 * @author shenzhao
 * @date 2023-03-30
 */
@RestController
@RequestMapping("/busi/user")
public class TMemberUserController extends BaseController
{
    @Autowired
    private ITMemberUserService tMemberUserService;

    /**
     * 查询用户列表
     */
    @PreAuthorize("@ss.hasPermi('busi:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(TMemberUser tMemberUser)
    {
        startPage();
        List<TMemberUser> list = tMemberUserService.selectTMemberUserList(tMemberUser);
        return getDataTable(list);
    }

    /**
     * 导出用户列表
     */
    @PreAuthorize("@ss.hasPermi('busi:user:export')")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMemberUser tMemberUser)
    {
        List<TMemberUser> list = tMemberUserService.selectTMemberUserList(tMemberUser);
        ExcelUtil<TMemberUser> util = new ExcelUtil<TMemberUser>(TMemberUser.class);
        util.exportExcel(response, list, "用户数据");
    }

    /**
     * 获取用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMemberUserService.selectTMemberUserById(id));
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('busi:user:add')")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMemberUser tMemberUser)
    {
        return toAjax(tMemberUserService.insertTMemberUser(tMemberUser));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('busi:user:edit')")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMemberUser tMemberUser)
    {
        return toAjax(tMemberUserService.updateTMemberUser(tMemberUser));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('busi:user:remove')")
    @Log(title = "用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMemberUserService.deleteTMemberUserByIds(ids));
    }
}

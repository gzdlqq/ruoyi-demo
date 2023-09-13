package com.ruoyi.web.controller.busi.admin;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.busi.domain.TSign;
import com.ruoyi.busi.service.ITSignService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * signController
 *
 * @author shenzhao
 * @date 2023-04-16
 */
@RestController
@RequestMapping("/busi/sign")
public class TSignController extends BaseController
{
    @Autowired
    private ITSignService tSignService;

    @Autowired
    private WxPayService wxService;

    /**
     * 查询sign列表
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSign tSign)
    {
        startPage();
        List<TSign> list = tSignService.selectTSignList(tSign);
        return getDataTable(list);
    }

    /**
     * 导出sign列表
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:export')")
    @Log(title = "sign", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSign tSign)
    {
        List<TSign> list = tSignService.selectTSignList(tSign);
        ExcelUtil<TSign> util = new ExcelUtil<TSign>(TSign.class);
        util.exportExcel(response, list, "sign数据");
    }

    /**
     * 获取sign详细信息
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tSignService.selectTSignById(id));
    }

    /**
     * 新增sign
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:add')")
    @Log(title = "sign", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSign tSign)
    {
        return toAjax(tSignService.insertTSign(tSign));
    }

    /**
     * 修改sign
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:edit')")
    @Log(title = "sign", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSign tSign)
    {
        return toAjax(tSignService.updateTSign(tSign));
    }

    /**
     * 删除sign
     */
    @PreAuthorize("@ss.hasPermi('busi:sign:remove')")
    @Log(title = "sign", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSignService.deleteTSignByIds(ids));
    }


    /**
     * 管理员审核拒绝 并退款
     */
    @ApiOperation(value = "订单退款,并修改订单状态为 已拒绝")
    @PutMapping("/reject")
    public AjaxResult reject(@RequestParam Long signId) throws WxPayException, WxErrorException {

        tSignService.reject(signId);

        //微信退款
        return AjaxResult.success("拒绝 并 退款成功~~~");
    }

    @ApiOperation(value = "管理员审核通过")
    @PutMapping("/access")
    public AjaxResult access(@RequestParam Long signId) throws WxPayException, WxErrorException {

        tSignService.access(signId);

        //审核通过
        return AjaxResult.success("审核通过");
    }
}

package com.ruoyi.web.controller.busi.wechat;

import cn.hutool.cache.impl.TimedCache;
import com.ruoyi.busi.cache.CommonCache;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.busi.mapper.TRaceSchemaMapper;
import com.ruoyi.busi.service.ITCompetitionService;
import com.ruoyi.busi.service.impl.TCompetitionServiceImpl;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.PageResp;
import com.ruoyi.common.core.domain.PageVo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "移动C端-赛事相关")
@RestController
@RequestMapping("/wechat/competition")
public class WechatCompetitionController extends BaseController
{
    @Autowired
    private ITCompetitionService tCompetitionService;

    @Autowired
    private TRaceSchemaMapper raceSchemaMapper;

    /**
     * 查询赛事列表
     */
    @PostMapping("/pageRace")
    @ApiOperation("查询赛事列表-假分页")
    public AjaxResult pageRace(PageVo pageVo,@RequestBody TCompetition competition)
    {
        PageResp pageResp = tCompetitionService.pageRace(pageVo, competition);

        return AjaxResult.success(pageResp);
    }

    /**
     * 查询赛事列表
     */
    @GetMapping("/getRaceDetailsById")
    @ApiOperation("查询详情 根据id")
    public AjaxResult getRaceDetailsById(@RequestParam Long id )
    {
        TCompetition competition = tCompetitionService.selectTCompetitionById(id);

        Long raceSchemaId = competition.getRaceSchemaId();
        //先从 缓存中 查询是否有这个 赛制id 对应的赛制 方案，如果有直接拿来中，如果没有，在查询
        if (raceSchemaId != null) {
            TimedCache<Long, TRaceSchema> formatTimedCache = CommonCache.formatTimedCache;
            TRaceSchema tRaceSchema = formatTimedCache.get(raceSchemaId, false);
            if (tRaceSchema == null) {
//                    ThreadUtil.sleep(1000); //为了让并发问题出现
                //双重检查锁
                synchronized (TCompetitionServiceImpl.class) {
                    //二次查询
                    tRaceSchema = formatTimedCache.get(raceSchemaId, false);
                    if (tRaceSchema == null) {
                        System.out.println("查询数据库");
                        tRaceSchema = raceSchemaMapper.selectTRaceSchemaById(raceSchemaId);
                        //缓存中不存在 查询出来要放进去
                        formatTimedCache.put(raceSchemaId, tRaceSchema);
                    }
                }
            }
            competition.setRaceSchema(tRaceSchema);
        }

        return AjaxResult.success(competition);
    }



}

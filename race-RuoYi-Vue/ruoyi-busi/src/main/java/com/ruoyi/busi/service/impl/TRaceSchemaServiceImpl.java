package com.ruoyi.busi.service.impl;

import java.util.List;
import java.util.function.Consumer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.busi.domain.resp.TRaceSchemaResp;
import com.ruoyi.busi.mapper.TCompetitionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.busi.mapper.TRaceSchemaMapper;
import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.busi.service.ITRaceSchemaService;

/**
 * 赛事方案Service业务层处理
 *
 * @author shenzhao
 * @date 2023-04-05
 */
@Service
public class TRaceSchemaServiceImpl implements ITRaceSchemaService
{
    @Autowired
    private TRaceSchemaMapper tRaceSchemaMapper;
    @Autowired
    private TCompetitionMapper competitionMapper;
    /**
     * 查询赛事方案
     *
     * @param id 赛事方案主键
     * @return 赛事方案
     */
    @Override
    public TRaceSchema selectTRaceSchemaById(Long id)
    {
        return tRaceSchemaMapper.selectTRaceSchemaById(id);
    }

    /**
     * 查询赛事方案列表
     *
     * @param tRaceSchema 赛事方案
     * @return 赛事方案
     */
    @Override
    public List<TRaceSchema> selectTRaceSchemaList(TRaceSchema tRaceSchema)
    {
        return tRaceSchemaMapper.selectTRaceSchemaList(tRaceSchema);
    }

    /**
     * 新增赛事方案
     *
     * @param tRaceSchema 赛事方案
     * @return 结果
     */
    @Override
    public int insertTRaceSchema(TRaceSchema tRaceSchema)
    {
        return tRaceSchemaMapper.insertTRaceSchema(tRaceSchema);
    }

    /**
     * 修改赛事方案
     *
     * @param tRaceSchema 赛事方案
     * @return 结果
     */
    @Override
    public int updateTRaceSchema(TRaceSchema tRaceSchema)
    {
        return tRaceSchemaMapper.updateTRaceSchema(tRaceSchema);
    }

    /**
     * 批量删除赛事方案
     *
     * @param ids 需要删除的赛事方案主键
     * @return 结果
     */
    @Override
    public int deleteTRaceSchemaByIds(Long[] ids)
    {
        return tRaceSchemaMapper.deleteTRaceSchemaByIds(ids);
    }

    /**
     * 删除赛事方案信息
     *
     * @param id 赛事方案主键
     * @return 结果
     */
    @Override
    public int deleteTRaceSchemaById(Long id)
    {
        return tRaceSchemaMapper.deleteTRaceSchemaById(id);
    }

    @Override
    public List<TRaceSchemaResp> querySchemaByCompetitionId(Long id) {
        //查询所有赛事方案
        List<TRaceSchema> tRaceSchemas = tRaceSchemaMapper.selectTRaceSchemaList(null);//null就是查询所有

        //数据类型转换
        List<TRaceSchemaResp> tRaceSchemaResps = BeanUtil.copyToList(tRaceSchemas, TRaceSchemaResp.class);

        //查询当前赛事是否绑定过方案
        TCompetition tCompetition = competitionMapper.selectTCompetitionById(id);
        Long raceSchemaId = tCompetition.getRaceSchemaId();


        //checked属性给值
        //parallelStream 是内置的多线程
        tRaceSchemaResps.parallelStream().forEach(tRaceSchemaResp -> {
            if (raceSchemaId == null) {
                tRaceSchemaResp.setChecked(false);
            }else{
                tRaceSchemaResp.setChecked(raceSchemaId.longValue()==tRaceSchemaResp.getId().longValue());
            }
        });

        return tRaceSchemaResps;
    }
}

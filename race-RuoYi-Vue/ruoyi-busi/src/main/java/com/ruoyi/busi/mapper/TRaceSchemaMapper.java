package com.ruoyi.busi.mapper;

import java.util.List;
import com.ruoyi.busi.domain.TRaceSchema;

/**
 * 赛事方案Mapper接口
 * 
 * @author shenzhao
 * @date 2023-04-05
 */
public interface TRaceSchemaMapper 
{
    /**
     * 查询赛事方案
     * 
     * @param id 赛事方案主键
     * @return 赛事方案
     */
    public TRaceSchema selectTRaceSchemaById(Long id);

    /**
     * 查询赛事方案列表
     * 
     * @param tRaceSchema 赛事方案
     * @return 赛事方案集合
     */
    public List<TRaceSchema> selectTRaceSchemaList(TRaceSchema tRaceSchema);

    /**
     * 新增赛事方案
     * 
     * @param tRaceSchema 赛事方案
     * @return 结果
     */
    public int insertTRaceSchema(TRaceSchema tRaceSchema);

    /**
     * 修改赛事方案
     * 
     * @param tRaceSchema 赛事方案
     * @return 结果
     */
    public int updateTRaceSchema(TRaceSchema tRaceSchema);

    /**
     * 删除赛事方案
     * 
     * @param id 赛事方案主键
     * @return 结果
     */
    public int deleteTRaceSchemaById(Long id);

    /**
     * 批量删除赛事方案
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRaceSchemaByIds(Long[] ids);
}

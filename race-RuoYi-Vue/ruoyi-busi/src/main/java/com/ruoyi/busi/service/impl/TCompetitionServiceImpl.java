package com.ruoyi.busi.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.github.pagehelper.PageInfo;
import com.ruoyi.busi.cache.CommonCache;
import com.ruoyi.busi.domain.TRaceSchema;
import com.ruoyi.busi.mapper.TRaceSchemaMapper;
import com.ruoyi.busi.utils.BusiUtils;
import com.ruoyi.common.core.domain.PageResp;
import com.ruoyi.common.core.domain.PageVo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.busi.mapper.TCompetitionMapper;
import com.ruoyi.busi.domain.TCompetition;
import com.ruoyi.busi.service.ITCompetitionService;

/**
 * 赛事Service业务层处理
 *
 * @author shenzhao
 * @date 2023-04-03
 */
@Service
public class TCompetitionServiceImpl implements ITCompetitionService {
    @Autowired
    private TCompetitionMapper tCompetitionMapper;

    @Autowired
    private TRaceSchemaMapper raceSchemaMapper;

    /**
     * 查询赛事
     *
     * @param id 赛事主键
     * @return 赛事
     */
    @Override
    public TCompetition selectTCompetitionById(Long id) {
        return tCompetitionMapper.selectTCompetitionById(id);
    }

    /**
     * 查询赛事列表
     *
     * @param tCompetition 赛事
     * @return 赛事
     */
    @Override
    public List<TCompetition> selectTCompetitionList(TCompetition tCompetition) {
        List<TCompetition> tCompetitions = tCompetitionMapper.selectTCompetitionList(tCompetition);

        //parallelStream 并行流，他有一个默认的线程池，多线程， 并发，效率高
        //多线程 查询中 随便用，增删改 慎用
        tCompetitions.parallelStream().forEach(new Consumer<TCompetition>() {
            @Override
            public void accept(TCompetition competition) {
//                ThreadUtil.sleep(500);
                //通过工具类，可以计算出 动态属性（赛事状态）
                BusiUtils.setCompetitionStatus(competition);
            }
        });

        getSchema(tCompetitions);


        return tCompetitions;
    }

    public void getSchema(List<TCompetition> tCompetitions) {
        tCompetitions.parallelStream().forEach(competition -> {
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
        });
    }

    /**
     * 新增赛事
     *
     * @param tCompetition 赛事
     * @return 结果
     */
    @Override
    public int insertTCompetition(TCompetition tCompetition) {
        tCompetition.setCreateTime(DateUtils.getNowDate());
        return tCompetitionMapper.insertTCompetition(tCompetition);
    }

    /**
     * 修改赛事
     *
     * @param tCompetition 赛事
     * @return 结果
     */
    @Override
    public int updateTCompetition(TCompetition tCompetition) {
        tCompetition.setUpdateTime(DateUtils.getNowDate());
        return tCompetitionMapper.updateTCompetition(tCompetition);
    }

    /**
     * 批量删除赛事
     *
     * @param ids 需要删除的赛事主键
     * @return 结果
     */
    @Override
    public int deleteTCompetitionByIds(Long[] ids) {
        return tCompetitionMapper.deleteTCompetitionByIds(ids);
    }

    /**
     * 删除赛事信息
     *
     * @param id 赛事主键
     * @return 结果
     */
    @Override
    public int deleteTCompetitionById(Long id) {
        return tCompetitionMapper.deleteTCompetitionById(id);
    }

    @Override
    public void publish(Long id) {
        TCompetition tCompetition = tCompetitionMapper.selectTCompetitionById(id);
        if (tCompetition.getRaceSchemaId() == null) {
            throw new ServiceException("还没绑定赛事方案，请先绑定赛事方案");
        }

        tCompetition.setBasePublished("1");//修改成发布状态
        int i = tCompetitionMapper.updateTCompetition(tCompetition);
    }

    /**
     * 方案二: 全量查询 + 假分页
     * 直接数据库查询出全部数据（能过滤的先过滤掉），然后转换出来每一条数据的状态。
     * 过滤掉不需要的数据。
     * 根据产品要求进行权重排序。
     * 基于内存做假分页 借助hutool工具类
     * @param competition
     */
    @Override
    public PageResp pageRace(PageVo pageVo, TCompetition competition) {
        //1.查询全部
        List<TCompetition> tCompetitions = tCompetitionMapper.selectTCompetitionList(null);
        //2.过滤掉不需要的数据  相当于是在数据库中，直接 where拼接了一个参数
        tCompetitions = tCompetitions.stream().filter(competition1 -> "1".equals(competition1.getBasePublished())).collect(Collectors.toList());
        //3.转换状态  串行  1W  100s   如果用了 parallel 可能只要10s
        tCompetitions.parallelStream().forEach(new Consumer<TCompetition>() {
            @Override
            public void accept(TCompetition competition) {
                //修改状态
                BusiUtils.setCompetitionStatus(competition);

                //4.权重排序
                //    @Excel(name = "状态 0.未发布 1.即将开始  2.报名中  3.编排中  4.比赛中  5.结束状态 ")
                //产品说 0的不要，上面已经过滤掉了
                //剩余的排序是   4 2 1 3 5
                //所谓的权重排序 就是 将当前这个  状态  转换成数字再去排序，我们排序的时候 使用权重排序，而不是 直接使用状态

                String status = competition.getStatus();
                if(status!=null){
                    //给出权重值
                    if("4".equals(status)){
                        competition.setQuanzhong(999L);
                    }else if("2".equals(status)){
                        competition.setQuanzhong(888L);
                    }else if("1".equals(status)){
                        competition.setQuanzhong(777L);
                    }else if("3".equals(status)){
                        competition.setQuanzhong(666L);
                    }else if("5".equals(status)){
                        competition.setQuanzhong(555L);
                    }
                }
            }
        });

//        System.out.println(tCompetitions);

        //排序
        tCompetitions = CollectionUtil.sort(tCompetitions, new Comparator<TCompetition>() {
            @Override
            public int compare(TCompetition o1, TCompetition o2) {
                if(o1.getQuanzhong() == null || o2.getQuanzhong() == null ){
                    return 0;
                }
                return (int) -(o1.getQuanzhong() - o2.getQuanzhong());
            }
        });

        int total = tCompetitions.size();

        //假分页
        tCompetitions = CollectionUtil.page(pageVo.getPageNum()-1, pageVo.getPageSize(), tCompetitions);


        //对有需要的数据再去查询~
        getSchema(tCompetitions);

        PageResp build = PageResp.builder()
                .list(Collections.unmodifiableList(tCompetitions))
//                .list(Collections.singletonList(tCompetitions))
                .pageNum(pageVo.getPageNum())
                .pageSize(pageVo.getPageSize())
                .totalCount(total)
                .build();

        System.out.println("测试热部署");

        return build;

    }
}

package com.ruoyi.web.controller.busi.wechat;

import com.ruoyi.busi.domain.TSession;
import com.ruoyi.busi.easyes.domain.Document;
import com.ruoyi.busi.easyes.mapper.DocumentMapper;
import com.ruoyi.busi.easyes.mapper.SessionMapper;
import com.ruoyi.busi.service.impl.TSignServiceImpl;
import com.ruoyi.common.annotation.NeedWechatLogin;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import org.dromara.easyes.core.core.EsWrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Consumer;

@Api(tags = "C端场次")
@RestController
@RequestMapping("/wechat/session")
public class WechatSessionController {

    @Autowired
    DocumentMapper documentMapper;

    @Autowired
    SessionMapper sessionMapper;

    @ApiOperation(value = "测试新增文档")
    @PostMapping("/testAddEsDoc")
    public AjaxResult testAddEsDoc() {

        // 测试插入数据
        Document document = new Document();
        document.setTitle("老汉");
        document.setContent("推*技术过硬");
        int successCount = documentMapper.insert(document);
        System.out.println(successCount);

        return AjaxResult.success(successCount);

    }


    @ApiOperation(value = "测试查询")
    @PostMapping("/testQueryEsDoc")
    public AjaxResult testQueryEsDoc() {

        // 测试查询 写法和MP一样 可以用链式,也可以非链式 根据使用习惯灵活选择即可
        String title = "老汉";
/*        List<Document> list = EsWrappers.lambdaChainQuery(documentMapper)
                .eq(Document::getTitle, title)
                .list();
        System.out.println(list);*/

        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(Document::getTitle, title);

        List<Document> list = documentMapper.selectList(wrapper);

        return AjaxResult.success(list);

    }


    @ApiOperation(value = "测试更新")
    @PostMapping("/testUpdateEsDoc")
    public AjaxResult testUpdateEsDoc() {

        // 测试更新
        String title = "老汉";
        List<Document> list = EsWrappers.lambdaChainQuery(documentMapper)
                .eq(Document::getTitle, title)
                .list();

        list.stream().forEach(new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                document.setContent("推*技术过硬,服务一流");
            }
        });
        int successCount = documentMapper.updateBatchByIds(list);
        System.out.println(successCount);

        // 测试更新 更新有两种情况 分别演示如下:
        // case1: 已知id, 根据id更新 (为了演示方便,此id是从上一步查询中复制过来的,实际业务可以自行查询)
/*        String id = "krkvN30BUP1SGucenZQ9";
        String title1 = "隔壁老王";
        Document document1 = new Document();
        document1.setId(id);
        document1.setTitle(title1);
        documentMapper.updateById(document1);*/

        // case2: id未知, 根据条件更新
/*        LambdaEsUpdateWrapper<Document> wrapper = new LambdaEsUpdateWrapper<>();
        wrapper.eq(Document::getTitle,"隔壁老王");
        Document document2 = new Document();
        document2.setTitle("隔壁老网吧");
        document2.setContent("推*技术过软");
        documentMapper.update(document2,wrapper);*/

        return AjaxResult.success(successCount);

    }

    @ApiOperation(value = "测试删除")
    @PostMapping("/testDeleteEsDoc")
    public AjaxResult testDeleteEsDoc() {

        // 测试删除
        String title = "老汉";
        List<Document> list = EsWrappers.lambdaChainQuery(documentMapper)
                .eq(Document::getTitle, title)
                .list();

        int successCount = documentMapper.deleteBatchIds(list.stream().map(Document::getId).collect(java.util.stream.Collectors.toList()));
        System.out.println(successCount);


        // 测试删除数据 删除有两种情况:根据id删或根据条件删
        // 鉴于根据id删过于简单,这里仅演示根据条件删,以老李的名义删,让老李心理平衡些
/*        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        String title = "隔壁老李";
        wrapper.eq(Document::getTitle,title);
        int successCount = documentMapper.delete(wrapper);
        System.out.println(successCount);*/

        return AjaxResult.success(successCount);

    }


    @ApiOperation(value = "循环赛对阵图")
    @PostMapping("/xunhuanSessions")
    public AjaxResult sign(Long competitionId) {

        //从ES中拿，和C端不一样
        List<TSession> tSessions = sessionMapper.selectList(
                new LambdaEsQueryWrapper<>(TSession.class)
                        .eq(TSession::getCompetitionId, competitionId));


        return AjaxResult.success(tSessions);

    }

}

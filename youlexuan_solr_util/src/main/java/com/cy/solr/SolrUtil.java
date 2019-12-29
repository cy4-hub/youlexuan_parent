package com.cy.solr;

import com.alibaba.fastjson.JSON;
import com.cy.mapper.TbItemMapper;
import com.cy.pojo.TbItem;
import com.cy.pojo.TbItemExample;
import com.github.promeg.pinyinhelper.Pinyin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.cy.solr</p>
 * <p>ClassName:SolrUtil</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:poker_heart
 * date:2019/11/27
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring-*.xml")
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;



    @Test
    public void importItemData(){
        TbItemExample ex=new TbItemExample();
        TbItemExample.Criteria c = ex.createCriteria();

        c.andStatusEqualTo("1");

        List<TbItem> itemList = itemMapper.selectByExample(ex);

        for(TbItem item : itemList){
            Map<String, Object> specMap= JSON.parseObject(item.getSpec());//将spec字段中的json字符串转换为map

            Map map = new HashMap();

            for(String key : specMap.keySet()) {
                map.put(Pinyin.toPinyin(key, "").toLowerCase(), specMap.get(key));
            }

            item.setSpecMap(map);
            System.out.println(item.getTitle()+">>>>"+item.getPrice()+">>>>"+item.getSpec());
        }

        System.out.println("--开始导入--");
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("--导入结束--");

    }

    @Test
    public void tes1(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }



}

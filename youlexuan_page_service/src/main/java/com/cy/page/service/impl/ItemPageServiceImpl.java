package com.cy.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cy.mapper.TbGoodsDescMapper;
import com.cy.mapper.TbGoodsMapper;
import com.cy.mapper.TbItemCatMapper;
import com.cy.mapper.TbItemMapper;
import com.cy.pojo.TbGoods;
import com.cy.pojo.TbGoodsDesc;
import com.cy.pojo.TbItem;
import com.cy.pojo.TbItemExample;
import com.cy.page.service.ItemPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.cy.page.service.impl</p>
 * <p>ClassName:ItemPageServiceImpl</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:poker_heart
 * date:2019/12/2
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */


@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Value("${pageDir}")
    private String pageDir;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private TbItemCatMapper itemCatMapper;


    @Override
    public boolean genItemHtml(Long goodsId) {

            Writer out=null;
        try{
            Configuration conf = freeMarkerConfigurer.getConfiguration();
            Template template = conf.getTemplate("item.ftl");

            Map map=new HashMap();


            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();

            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);


            TbItemExample ex=new TbItemExample();
            TbItemExample.Criteria c = ex.createCriteria();
            c.andGoodsIdEqualTo(goodsId);
            c.andStatusEqualTo("1");
            ex.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(ex);

            map.put("goods", goods);
            map.put("itemCat1Name", itemCat1Name);
            map.put("itemCat2Name", itemCat2Name);
            map.put("itemCat3Name", itemCat3Name);
            map.put("goodsDesc", goodsDesc);
            map.put("itemList", itemList);

            out=new FileWriter(pageDir+goodsId+".html");

            template.process(map,out);

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

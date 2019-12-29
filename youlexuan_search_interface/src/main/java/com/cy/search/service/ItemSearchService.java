package com.cy.search.service;

import com.cy.pojo.TbItem;


import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    // 查询solr索引库
    public Map<String, Object> search(Map searchMap);

    // 新增solr索引库
    public void importData(List<TbItem> list);

    // 删除solr索引库：ids表示多个tb_item的id
    public void deleteData(Long[] ids);

}

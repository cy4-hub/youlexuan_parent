package com.cy.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cy.content.service.ContentService;
import com.cy.mapper.TbContentMapper;
import com.cy.pojo.TbContent;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.cy.entity.PageResult;
import com.cy.pojo.TbContentExample;
import com.cy.pojo.TbContentExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * content服务实现层
 * @author cy
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;



	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {

		List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
		if (contentList == null) {
			System.out.println("从数据库读取数据放入缓存");
			TbContentExample contentExample = new TbContentExample();
			Criteria criteria = contentExample.createCriteria();
			criteria.andCategoryIdEqualTo(categoryId);
			criteria.andStatusEqualTo("1");//开启状态
			contentExample.setOrderByClause("sort_order");//排序
			contentList = contentMapper.selectByExample(contentExample);//获取广告列表
			redisTemplate.boundHashOps("content").put(categoryId, contentList);//存入缓存
		}else{
			System.out.println("从缓存读取数据");
		}
		    return  contentList;
		}


	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 分页
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {

		contentMapper.insert(content);

		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}


	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		Long categoryId = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
		redisTemplate.boundHashOps("content").delete(categoryId);

		contentMapper.updateByPrimaryKey(content);

		if(categoryId.longValue()!=content.getCategoryId().longValue()){
			redisTemplate.boundHashOps("content").delete(content.getCategoryId());
		}
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {

		for(Long id:ids){
			Long categoryId = contentMapper.selectByPrimaryKey(id).getCategoryId();//广告分类ID
			redisTemplate.boundHashOps("content").delete(categoryId);

			contentMapper.deleteByPrimaryKey(id);
		}		
	}
	
	/**
	 * 分页+查询
	 */
	@Override
	public PageResult findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content != null){			
						if(content.getTitle() != null && content.getTitle().length() > 0){
				criteria.andTitleLike("%" + content.getTitle() + "%");
			}			if(content.getUrl() != null && content.getUrl().length() > 0){
				criteria.andUrlLike("%" + content.getUrl() + "%");
			}			if(content.getPic() != null && content.getPic().length() > 0){
				criteria.andPicLike("%" + content.getPic() + "%");
			}			if(content.getStatus() != null && content.getStatus().length() > 0){
				criteria.andStatusLike("%" + content.getStatus() + "%");
			}
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}

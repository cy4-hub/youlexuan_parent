package com.cy.entity;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.cy.entity</p>
 * <p>ClassName:PageResult</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:poker_heart
 * date:2019/11/15
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class PageResult implements Serializable {

    private Long total;

    private List<?> rows;

    public PageResult() {

    }

    public PageResult(Long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}

package com.cy.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.pojo.TbSeller;
import com.cy.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.cy.shop.service</p>
 * <p>ClassName:UserDetailsServiceImpl</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:poker_heart
 * date:2019/11/20
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("----经过UserDetailsServiceImpl----");
        // 构建角色列表
        List<GrantedAuthority> grantAuths = new ArrayList<GrantedAuthority>();
        grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        System.out.println(username);
        // 得到商家对象
        TbSeller seller = sellerService.findOne(username);

        if (seller != null) {
            // 审核通过
            if (seller.getStatus().equals("1")) {
                return new User(username, seller.getPassword(), grantAuths);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}


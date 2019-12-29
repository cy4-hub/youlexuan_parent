package com.cy.shop.controller;

import com.cy.entity.Result;
import com.cy.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * All rights Reserved, Designed By www.info4z.club
 * <p>title:com.cy.shop.controller</p>
 * <p>ClassName:UploadController</p>
 * <p>Description:TODO(请用一句话描述这个类的作用)</p>
 * <p>Compony:Info4z</p>
 * author:poker_heart
 * date:2019/11/22
 * version:1.0
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        //1、取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        try {
            //2、创建一个 FastDFS客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");

            //3、执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);

            //4、拼接返回的图片路径 和 ip 地址成完整的 url
            String url = FILE_SERVER_URL + path;

            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}

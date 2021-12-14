package com.guking.blog.controller;

import com.guking.blog.util.QiniuUtils;
import com.guking.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadPictureController {

    @Autowired
    private QiniuUtils qiniuUtils;

    /**
     * 上传图片
     * @RequestParam("image") MultipartFile file 用来接收上传文件名字
     * @param file
     * @return
     */
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        // 原始文件名称 比如: aa.png  那拿到的就算aa.png
        String filename = file.getOriginalFilename();
        // 唯一的文件名称,   UUID唯一性,   StirnUtils.substringAfterLast(filename,".") 截取最后一个点后面的字符,比如: aa.aa.png  ---> 截取png
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(filename, ".");
        // 上传到七牛云 云服务器上
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }
}

package com.cqSwuNo16.demo.controller;/**
 * @description TODO
 * @date 2022-09-15 2:49
 * @author wu yunChuan
 */

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @BelongsProject: demo
 * @BelongsPackage: com.cqSwuNo16.demo.controller
 * @Author: wu yunChuan
 * @CreateTime: 2022-09-15  02:49
 * @Description: 通过Nginx反向代理向服务器上传图片
 * @Version: 1.0
 * @Email: 1458200660@qq.com
 */
@RestController
@RequestMapping("/upload")
public class UploadPicsController {
    /**
     * 访问服务器文件路径端口
     */
    @Value(value = "${filePath}")
    private String imgPath;
    /**
     * 服务器保存文件路径
     */
    @Value(value = "${uploadHostFilePath}")
    private String uploadHost;

    @GetMapping("/")
    public String welcome() {
        return "welcome to pic_server";
    }

    /**
     * 项目host路径
     */
    @PostMapping(value = "/upImg")
    public JSONObject upImg(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject();
        byte[] bfile = JSONObject.parseObject(json).getBytes("data");
        String fileName = JSONObject.parseObject(json).getString("fileName");
        byte2File(bfile, uploadHost, fileName);
        if (json.isEmpty()) {
            return null;
        }
        try {
            System.out.println("上传成功");
            jsonObject.put("data", imgPath + fileName);
            return jsonObject;
        } catch (Exception e) {
            System.out.println("上传失败");
            jsonObject.put("message", e.toString());
        }
        return jsonObject;
    }

    //byte数组转file文件方法
    public static void byte2File(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

}

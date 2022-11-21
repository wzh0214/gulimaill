package com.wzh.gulimallThirdparty;

import com.aliyun.oss.OSSClient;
import com.wzh.gulimall.thirdparty.GuliMallThirdPartyApplication;

import com.wzh.gulimall.thirdparty.component.SmsComponent;
import com.wzh.gulimall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wzh
 * @data 2022/10/31 -17:45
 */
@SpringBootTest(classes = GuliMallThirdPartyApplication.class)
public class GuliMallThirdPartyTest {
    @Autowired
    OSSClient ossClient;

    @Autowired
    SmsComponent smsComponent;


    @Test
    public void testUpload() throws FileNotFoundException {

        //上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\14368\\Pictures\\Saved Pictures\\1.jpg");
        ossClient.putObject("gulimall-178", "penguin5.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功.");
    }


    @Test
    public void testSms() {
        String host = "http://smsyun.market.alicloudapi.com";
        String path = "/sms/sms01";
        String method = "POST";
        String appcode = "11db853e9ac945d7811fadb90fabbb2d";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", "【云信】您的验证码是：147258");
        querys.put("mobile", "13326166085");
        String bodys = "";


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSms2() {
        smsComponent.sendSmsCode("17816120149", "123456");
    }

}

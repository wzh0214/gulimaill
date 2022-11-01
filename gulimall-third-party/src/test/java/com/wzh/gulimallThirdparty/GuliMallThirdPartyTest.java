package com.wzh.gulimallThirdparty;

import com.aliyun.oss.OSSClient;
import com.wzh.gulimall.thirdparty.GuliMallThirdPartyApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author wzh
 * @data 2022/10/31 -17:45
 */
@SpringBootTest(classes = GuliMallThirdPartyApplication.class)
public class GuliMallThirdPartyTest {
    @Autowired
    OSSClient ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {

        //上传文件流。
        InputStream inputStream = new FileInputStream("C:\\Users\\14368\\Pictures\\Saved Pictures\\1.jpg");
        ossClient.putObject("gulimall-178", "penguin5.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功.");
    }
}

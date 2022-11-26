package com.wzh.gulimallSearch;

import com.alibaba.fastjson.JSON;
import com.wzh.gulimall.search.GuliMallSearchApplication;
import com.wzh.gulimall.search.config.GulimallElasticsearshConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * @author wzh
 * @data 2022/11/9 -19:58
 */

@SpringBootTest(classes = GuliMallSearchApplication.class)
public class test {
   @Autowired
    RestHighLevelClient restHighLevelClient;

   @Test
    public void s() {
       System.out.println(restHighLevelClient);
   }

   @Test
   public void searchData() {
       // 1.创建检索请求
       SearchRequest searchRequest = new SearchRequest();
       // 2.指定索引
       searchRequest.indices("bank");
       // 指定检索的条件
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));




   }

   @Test
    public void indexData() throws IOException {
       IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1"); // 数据的id
       User user = new User();
       user.setAge(22);
       user.setUserName("lisi");
       user.setGender("M");
       String s = JSON.toJSONString(user); // 要转为json
       indexRequest.source(s, XContentType.JSON); //要保存的内容

       // 执行操作
       IndexResponse index = restHighLevelClient.index(indexRequest, GulimallElasticsearshConfig.COMMON_OPTIONS);

       System.out.println(index);

   }

   @Data
   class User {

       private String userName;
       private String gender;
       private Integer age;
   }
}

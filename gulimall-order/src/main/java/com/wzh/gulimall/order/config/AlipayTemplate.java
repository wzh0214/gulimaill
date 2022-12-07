package com.wzh.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.wzh.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000121693661";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCvKORWtr3VmLNtiNYnWEIZMYNY8huTSne2vyK5/QjG46Y2ErTHyVe44/CDh+VyZCUqC538JsR3iP7lXajvgofBmcaMR77v9urXWHVVZtrJOMQ5G2kyFIysQqdIewWPLism/SXPea8rtfio5LiEPXfH8AYvYcYoDOfkK/yHqdBrL6eWFf1aWul7yF7exZ2eMYlz+8C2VnUqoj25DBYGnqK6JtuXqOqBjeUEtziecYqfKxssFhjSvyVKOP4ws8acf8ZCoMDjyNg/a5DKDJ+2BLniMaSzhSenE+ZnBiXv9Whewyhhs0Eu3I9pj1kL/XjPmEIqKwgnPFhD7Se+tyP2zjbvAgMBAAECggEAPGGcVUgpqLg7IkdJi8ahAo5O+Rp85wmEuzqoffT8UZOoeHt6VmTSGKp6/tyZ/BUJurTGgWXb1FklhWwEEIyuv86C41jYOWClFBcqW4ZZZYRsIEOSv/DVjYxsnk/KWtFPjuE1Z5BLEoZocdBmyy9lW0ci2rKZ3LSWO9Ht+9ZOQqRW31GwI0ZgivNWvsI6GMFuJ/evtOdsmWHu6Og3SA8KMaSnAZAg0NJ3gHXD5EAfD8Ff2QVOStvlCAL6i272hto1iTF0YsImv6Ys01xziXNagsw12aRUc0gOLILMpLxXpFVxdC8aZSOnyLoCQ0klJVkDeflsWw+UQfQiTUHTxhsKAQKBgQD58IrAhYN5Y9ydjVuH0QXQaOYo25qGo1wIlHTGIXaF18IUdyhSrFtIaqOogNbb5cTAWhJWu+G5wGZuJXKbOKmBXOwAOoG4AFWG/KHmfNIFUMSlo1gWzbFs/nE6H8iHH9bHWjuFClAqiHV3+6w67qghA+t18zWuTJ0fquFlo4q4dQKBgQCzaCqb9ZVmeCYdk9nZTZWGiNeujlMfG8czkdYKc/Ow0eh8uOW7uFVT3bvDT5kxzsnlCW9ejVJcoJNwsk7tIxp64xavBu/+JBtWBeDa4QG1Z6ZfFmgCzXVQM3Lp6i5fD/+2fsJEQNqSEJUKbnc4Rn0VVLJyD8+8n3eB5by2Qg2lUwKBgGuXtOMzlmlHJ81a8LACtvIEdi/YeSjEYfX/klVISnFLc+zDbsRIFfOSlGkRLWzz+EKlg4d60azYuj9oN3HCbpWVnrJqfTTsT+UQE9HYocBQCJcVPfJzEZHN5ogS7ovclOS0XzLiQahNnePMn1OvMQklWtMno1ApAuqC8QyD3k+RAoGAH8TBTs6k/vklnrUwrVcctC8FKkbPl0Ge+5XGdiFkNJpm9bb0D1Kr4WbxObudoAFDJQiPrQmPXRGeKiWY7kr6BGqz+Jhc5P7VzFzMHVYcrFfHUS0xi9WAAYyqu5sZTQITQOifU+C8E4NWMPuwpslsmttdoe8E1RzT16+rxAZcTjsCgYEA5pRdNdWhdXm43wKeoJ4KOre9sNFddFMxT/tNnpRTpFrTBzZSaRUIQpsYhelQ9cw64ad/h6MqJPQGwJBfGgm0cw09QuZ8+xWYAfXby/PhUbspU9l1P9dHyj1CMjjdbY4VFzr8iqiYHLExNhYNsrFyfLNeJ8NZpJtbvZPjc1zKkak=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcfDd9453+5iwr1Em+O3YEHqm69K7u9e4BJwjqQBMMXiwJHdz+pwgbTi5Dnw51DR5aN5BYy8bgG/MA6LvfkDvBHsylWvOqNtHRbbD1IK34Ru101EUXJCRp7VigLl0jWGDtCixdWxQ9jX0+lsl3rvA8sqpHGgwKgOxvJQNUP00heYlBDzvAWvjayO8sxO4+O1bRKsHZCD2Fm23KwCVieEWTseTe82NtG3C44AQ9uhmbwm4CYjCS4B5Z9JntqxIGUZyRa9swuJdLKC5Lq0GuI6wYyZeB9xwn99F6pwaEwD1czZB7+z+OWenI3F7vwmWxWM0RekJS1alNDT4Sz7Tzv9YwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url;

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}

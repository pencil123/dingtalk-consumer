package cn.blogs.dingtalkconsumer.dingtalk;

import cn.blogs.dingtalkconsumer.config.MapConfig;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Component
public class MsgSend2 {
    @Autowired
    private MapConfig mapConfig;
    private int counter =0;
    Calendar firstTime = Calendar.getInstance();
    private Map<String,String> urlsMap;
    private Logger logger = LoggerFactory.getLogger(MsgSend2.class);

    public String send(JSONObject message) {
        urlsMap = mapConfig.getUrls();
        String target = message.get("target") !=  null ? message.get("target").toString() : "default";
        logger.info("target is :{}",target);
        logger.info("urls info :{}",urlsMap.toString());
        logger.info("target dingtalk is :{}",urlsMap.get(target));

        synchronized(urlsMap.get(target)) {
            Long time = Long.valueOf(0);
            if (counter == 0) {
                firstTime = Calendar.getInstance();
                counter++;
            } else if (counter == 18) {
                time = Calendar.getInstance().getTimeInMillis() - firstTime.getTimeInMillis();
                if (time < 60000) {
                    try {
                        Thread.sleep(60000 - time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                counter = 0;
            } else {
                counter++;
            }
        }
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        logger.info(message.toJSONString());

        HttpPost httpPost = new HttpPost(urlsMap.get(target));
        message.remove("target");
        StringEntity requestEntity = new StringEntity(message.toJSONString(),"utf-8");
        requestEntity.setContentEncoding("UTF-8");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(requestEntity);
        try{
            HttpResponse response  = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            returnValue = EntityUtils.toString(entity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("钉钉调用返回信息: {};counter:{}",returnValue,counter);
        try {
            httpclient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnValue;
    }
}

package cn.blogs.dingtalkconsumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Component
@ConfigurationProperties(prefix = "dingtalk")
@EnableConfigurationProperties(MapConfig.class)
public class MapConfig {

    /**
     * 从配置文件中读取的dingtalk.urls开头的数据
     * 注意：名称必须与配置文件中保持一致
     */
    private Map<String, String> urls;

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }

}

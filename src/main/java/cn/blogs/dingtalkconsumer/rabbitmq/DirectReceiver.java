package cn.blogs.dingtalkconsumer.rabbitmq;

import cn.blogs.dingtalkconsumer.dingtalk.MsgSend2;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "dingtalkQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgSend2 msgSend2;

    @RabbitHandler
    public void process(String stringMessage) {
        msgSend2.send(JSON.parseObject(stringMessage));
        System.out.println("DirectReceiver消费者收到消息  : " + stringMessage);
    }
}

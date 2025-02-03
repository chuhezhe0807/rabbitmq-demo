package com.chuhezhe.mq.listener;

import com.chuhezhe.mq.Constants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * ClassName: MyMessageListener
 * Package: com.chuhezhe.mq.listener
 * Description:
 *
 * @Author Chuhezhe
 * @Create 2025/2/3 19:46
 * @Version 1.0
 */
@Component
@Slf4j
public class MyMessageListener {

    // 以下写法是在 监听+在RabbitMQ服务器上创建交换机和队列，如果已经床架好了交换机和队列，则只需要 @RabbitListener(queues = {QUEUE_NAME})
    @RabbitListener(
            // 设置绑定关系
            bindings = @QueueBinding(
                    // 指定队列的信息 value为队列的名称 durable表示是否需要持久化
                    value = @Queue(value = Constants.QUEUE_NAME, durable = "true"),
                    // 交换机
                    exchange = @Exchange(value = Constants.EXCHANGE_DIRECT),
                    // 路由键信息
                    key = {Constants.ROUTING_KEY}
            )
    )
    public void processMessage(
            String dataString,      // 消息内容，本demo中使用的都是字符串消息，所以这里是String类型
            Message message,        // 代表消息的对象
            Channel channel         // 频道对象
    ) {
        log.info("消费端接收到了消息，{}", dataString);
    }
}

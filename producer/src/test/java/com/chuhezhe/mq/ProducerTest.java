package com.chuhezhe.mq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName: ProducerTest
 * Package: com.chuhezhe.mq
 * Description:
 *
 * @Author Chuhezhe
 * @Create 2025/2/3 20:56
 * @Version 1.0
 */
@SpringBootTest
public class ProducerTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    // 发送消息
    @Test
    public void testSendMessage() {
        rabbitTemplate.convertAndSend(
                Constants.EXCHANGE_DIRECT,      // 交换机名称
                Constants.ROUTING_KEY + "~",    // 路由键，写错路由键，达到成功找到交换机，但是没有匹配路由键的队列，这时就会去找备份交换机，将消息发送到与备份交换机绑定的队列中
                "hello, spring boot rabbitmq."  // 发送的消息
        );
    }
}

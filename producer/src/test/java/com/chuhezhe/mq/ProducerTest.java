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
                Constants.EXCHANGE_DIRECT + "1",      // 交换机名称，测试交换机名称错误，发送到交换机失败的场景
                Constants.ROUTING_KEY,          // 路由键
                "hello, spring boot rabbitmq."  // 发送的消息
        );
    }
}

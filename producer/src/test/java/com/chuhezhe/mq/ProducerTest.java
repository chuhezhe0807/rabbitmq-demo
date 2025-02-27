package com.chuhezhe.mq;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Resource
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

    // 发送100条消息，测试
    @Test
    public void test02() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(Constants.EXCHANGE_DIRECT, Constants.ROUTING_KEY, "test prefetch " + i);
        }
    }

    // 测试队列消息超时
    @Test
    public void test03Timeout() {
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(Constants.EXCHANGE_TIMEOUT, Constants.ROUTING_KEY_TIMEOUT, "test timeout " + i);
        }
    }

    // 设置单个消息超时
    @Test
    public void test04Timeout() {
        // 创建消息后置处理器对象
        MessagePostProcessor messagePostProcessor = (message) -> {
            // 设置消息单独的过期时间，单位毫秒
            message.getMessageProperties().setExpiration("7000");

            return message;
        };

        rabbitTemplate.convertAndSend(Constants.EXCHANGE_TIMEOUT, Constants.ROUTING_KEY_TIMEOUT, "test timeout",
                messagePostProcessor);
    }

    // 死信产生的情况2：超出队列最大容量
    // 死信产生的情况3：超时（此时关闭消费者端，消息队列定义了10s的超时时间，超时后就会成为死信）
    @Test
    public void test05() {
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(
                Constants.EXCHANGE_NORMAL,
                Constants.ROUTING_KEY_NORMAL,
                "死信产生的情况2：超出队列最大容量 " + i
            );
        }
    }

    // 测试基于插件的延迟消息
    @Test
    public void test06DelayMessageByPlugins() {
        // 消息后置处理器
        MessagePostProcessor messagePostProcessor = (message) -> {
            // 设置超时时间，单位毫秒
            // "x-delay" 只有 rabbitmq_delayed_message_exchange 插件才能识别，且交换机类型要为 x-delayed-message
            message.getMessageProperties().setHeader("x-delay", "10000");

            return message;
        };

        rabbitTemplate.convertAndSend(
                Constants.EXCHANGE_DELAY,
                Constants.ROUTING_KEY_DELAY,
                "test delay by plugin " + new SimpleDateFormat("HH:mm:ss").format(new Date()),
                messagePostProcessor
        );
    }

    // 优先级消息
    @Test
    public void test07PriorityMessage() {
        for (int i = 1; i <= 5; i++) {
            int priority = i;

            rabbitTemplate.convertAndSend(
                    Constants.EXCHANGE_PRIORITY,
                    Constants.ROUTING_KEY_PRIORITY,
                    "优先级消息 " + i,
                    (message) -> {
                        // priority 越大优先级越高
                        message.getMessageProperties().setPriority(priority); // priority不能超过队列设置的 x-max-priority 值

                        return message;
                    }
            );
        }
    }
}

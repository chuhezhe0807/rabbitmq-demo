package com.chuhezhe.mq.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: MQProducerAckConfig
 * Package: com.chuhezhe.mq.config
 * Description:
 *
 * @Author Chuhezhe
 * @Create 2025/2/3 21:37
 * @Version 1.0
 */
@Slf4j
@Configuration
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // MQProducerAckConfig bean 初始化后调用
    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    // 发送消息到交换机成功或失败时调用这个方法，例如交换机名称没有匹配任何一个已存在的交换机时会执行这个方法
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("Confirm 回调打印 CorrelationData: {}, ack: {}, cause: {}", correlationData, ack, cause);
    }

    // 发送到队列失败才会调用这个方法，例如路由键没有匹配任何一个已经绑定了交换机和队列的路由键时会执行这个方法
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息主体：{}, 应答码：{}, 描述：{}, \n 消息使用的交换机 exchange：{}, 消息使用的路由键 routing：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getReplyCode(),
                returnedMessage.getReplyText(),
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey()
        );
    }
}

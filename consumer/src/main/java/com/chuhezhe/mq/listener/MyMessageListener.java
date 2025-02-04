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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
//    @RabbitListener(
//            // 设置绑定关系
//            bindings = @QueueBinding(
//                    // 指定队列的信息 value为队列的名称 durable表示是否需要持久化
//                    value = @Queue(value = Constants.QUEUE_NAME, durable = "true"),
//                    // 交换机
//                    exchange = @Exchange(value = Constants.EXCHANGE_DIRECT),
//                    // 路由键信息
//                    key = {Constants.ROUTING_KEY}
//            )
//    )
//    @RabbitListener(queues = {Constants.QUEUE_NAME})
    public void processMessage(
            String dataString,      // 消息内容，本demo中使用的都是字符串消息，所以这里是String类型
            Message message,        // 代表消息的对象
            Channel channel         // 频道对象
    ) throws IOException {
        // 获取消息的deliveryTag
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            log.info("消费端接收到了消息，{}", dataString);

            // 返回ack确认
            // long deliveryTag, 交付标签 每一个消息进入队列时，broker都会生成一个64位整数的唯一标识，这个就是deliveryTag
            //      交付标签作用：消费端把消息处理结果ack，nack，reject等返回给broker之后，broker需要对对应的消息执行后续操作，
            //      例如删除消息、重新排队或标记为死信等等。那么broker就必须知道她现在要操作的消息具体是哪一条，deliveryTag作为唯一标识就很好的满足了这个需求。
            //
            // boolean multiple 指定某个deliveryTag，multiple为true时，批量处理前面的所有消息，为false时，只处理这一条消息，一般为false
            channel.basicAck(deliveryTag, false);
        }
        catch(Exception e) {
            // 模拟消息处理程序认为此次消息处理未成功的情况，未成功可以选择重试重新投递

            // 重复投递过一次了，仍然失败了，所以可以直接丢弃
            // 获取当前消息是否是重复投递的，如果是则broker不需要放回队列，直接丢弃就可以了
            Boolean redelivered = message.getMessageProperties().getRedelivered();

            // 返回nack确认
            // requeue 控制消息是否重新放回队列。为true，重新放回队列，broker会重新投递这个消息。为false，不重新放回队列，broker会丢弃这个消息
            channel.basicNack(deliveryTag, false, !redelivered);

            // basicNack与basicReject都是返回nack，区别在于：basicNack能控制是否批量操作，basicReject不能
        }

    }

    // 测试慢一点消费消息
    @RabbitListener(queues = {Constants.QUEUE_NAME})
    public void processMessageTestPrefetch(String dataString, Message message, Channel channel) throws IOException, InterruptedException {
        log.info("消费端接收到了消息，{}", dataString);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        TimeUnit.SECONDS.sleep(1);

        channel.basicAck(deliveryTag, false);
    }
}

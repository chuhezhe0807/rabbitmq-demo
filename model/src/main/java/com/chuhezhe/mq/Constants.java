package com.chuhezhe.mq;

/**
 * ClassName: Constants
 * Package: com.chuhezhe.mq
 * Description:
 *
 * @Author Chuhezhe
 * @Create 2025/2/3 20:51
 * @Version 1.0
 */
public interface Constants {
    String EXCHANGE_DIRECT = "exchange.direct.order";

    String EXCHANGE_TIMEOUT = "exchange.test.timeout";

    String EXCHANGE_NORMAL = "exchange.normal";

    String EXCHANGE_DEAD_LETTER = "exchange.dead.letter";

    // 基于 rabbitmq_delayed_message_exchange 插件的延迟消息交换机，类型为x-delayed-message，并且带有参数arguments: x-delayed-type:	direct
    String EXCHANGE_DELAY = "exchange.test.delay";

    String ROUTING_KEY_DELAY = "routing.key.delay";

    String ROUTING_KEY_DEAD_LETTER = "routing.key.dead.letter";

    String ROUTING_KEY_NORMAL = "routing.key.normal";

    String ROUTING_KEY_TIMEOUT = "routing.key.test.timeout";

    String ROUTING_KEY = "order";

    String QUEUE_NAME = "queue.order";

    String QUEUE_DEAD_LETTER = "queue.dead.letter";

    String QUEUE_NORMAL = "queue.normal";

    String QUEUE_DELAY = "queue.test.delay";
}

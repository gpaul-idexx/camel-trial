package cameltrial

import groovy.util.logging.Slf4j
import org.apache.camel.CamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
/**
 * Send and receive messages with camel using rabbit as a broker.
 * @author gpaul
 * @since Apr 22, 2016
 */
@Slf4j
class RabbitSimple {
    static void main(String[] args) {

        Queue queue = new Queue(
                exchange: 'camel-trial-exchange',
                queue: 'camel-trial-queue',
                host: 'localhost'
        )

        CamelContext context = new DefaultCamelContext()
        context.addRoutes(new RouteBuilder() {
            @Override
            void configure() throws Exception {
                from("rabbitmq://${queue.host}/${queue.exchange}?queue=${queue.queue}")
                        .transform(body().append('\n'))
                        .to("stream:out")
            }
        })
        context.addRoutes(new RouteBuilder() {
            @Override
            void configure() throws Exception {
                from("timer://simple?period=1000").setBody().simple('Test message: ${id}')
                        .to("rabbitmq:${queue.host}/${queue.exchange}?queue=${queue.queue}")
            }
        })
        context.start()
    }

    static class Queue {
        String host
        String queue
        String exchange
    }
}

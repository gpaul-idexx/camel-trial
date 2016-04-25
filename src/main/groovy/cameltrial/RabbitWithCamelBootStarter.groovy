package cameltrial

import groovy.util.logging.Slf4j
import org.apache.camel.builder.RouteBuilder
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
/**
 * Send and receive messages with camel using rabbit as a broker in a spring boot app. Notable
 * here is that we're taking advantage of camel-spring-boot-starter. We simply create a spring
 * bean for each route, and they're automatically added to the {@link CamelContext}.
 *
 * More docs available: https://camel.apache.org/spring-boot.html
 *
 * @author gpaul
 * @since Apr 22, 2016
 */
@Slf4j
@SpringBootApplication
class RabbitWithCamelBootStarter {
    static void main(String[] args) {
        SpringApplication.run RabbitWithCamelBootStarter, args
    }

    @Bean
    RouteBuilder producer() {
        new RouteBuilder() {
            @Override
            void configure() throws Exception {
                from("timer://simple?period=1000").setBody().simple('Test message: ${id}')
                        .to("rabbitmq:{{rabbit.host}}/{{rabbit.exchange}}?queue={{rabbit.queue}}");
            }
        }
    }

    @Bean
    RouteBuilder consumer() {
        new RouteBuilder() {
            @Override
            void configure() throws Exception {
                //{{rabbit.*}} is coming from application.yml, the camel starter hooks that up for us
                from("rabbitmq://{{rabbit.host}}/{{rabbit.exchange}}?queue={{rabbit.queue}}")
                        .transform(body().append('\n'))
                        .to("stream:out")
            }
        }
    }

}

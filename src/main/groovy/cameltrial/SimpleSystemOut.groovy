package cameltrial

import groovy.util.logging.Slf4j
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.main.Main
import org.apache.camel.main.MainListenerSupport
import org.apache.camel.main.MainSupport
/**
 *
 * Just to prove we can get something working, we'll write to system out. Using camel's {@link Main} to take care
 * of start up and shut down of the camel context.
 *
 * @author gpaul
 * @since Apr 22, 2016
 */
@Slf4j
class SimpleSystemOut {

    private Main main;

    static void main(String[] args) {
        def trial = new SimpleSystemOut()
        trial.boot()
    }

    void boot() throws Exception {
        main = new Main()
        main.addRouteBuilder(new RouteBuilder() {
            @Override
            void configure() throws Exception {
                from("timer://simple?period=1000")
                        .setBody().simple('Test message: ${id}')
                        .to("stream:out");

            }
        })
        main.addMainListener(new Events());

        main.run()
    }

    static class Events extends MainListenerSupport {

        @Override
        public void afterStart(MainSupport main) {
            log.info("It's been started");
        }

        @Override
        public void beforeStop(MainSupport main) {
            log.info("It's going to shut down");
        }
    }
}

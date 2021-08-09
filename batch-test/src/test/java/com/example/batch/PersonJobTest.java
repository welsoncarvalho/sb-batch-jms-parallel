package com.example.batch;

import com.example.batch.config.TestIntegrationConfig;
import com.example.batch.config.TestSlaveConfig;
import com.example.batch.container.ArtemisContainer;
import com.example.batch.container.MysqlContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import javax.jms.ConnectionFactory;

@SpringBootTest(
//        classes = {
//                TestIntegrationConfig.class,
////                TestSlaveConfig.class
//        }
)
@ContextConfiguration(
        initializers = PersonJobTest.ContainersInitializer.class
)
public class PersonJobTest {

//    static ArtemisContainer artemisContainer = new ArtemisContainer();
    static MysqlContainer mysqlContainer = new MysqlContainer();

//    @Autowired
//    MessageChannel inPersonSaveQueue;

    @Autowired
    ConnectionFactory connectionFactory;

    @BeforeAll
    public static void setup() {
//        artemisContainer.start();
        mysqlContainer.start();
    }

    @AfterAll
    public static void tearDown() {
//        artemisContainer.stop();
        mysqlContainer.stop();
    }

    @Test
    void testPersonStep() throws Exception {
//        ChunkRequest<String> request = new ChunkRequest<>(1,
//                List.of("Raymond,Jordan,fu@sechem.gg,20/8/1982"), 1L,
//                new StepContribution(new StepExecution("step_execution", new JobExecution(1L))));
//        inPersonSaveQueue.send(new GenericMessage<>(request));
    }

    public static class ContainersInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
//                    "spring.artemis.host=" + artemisContainer.getContainerIpAddress(),
//                    "spring.artemis.port=" + artemisContainer.getPort(),
                    "spring.datasource.url=jdbc:mysql://" + mysqlContainer.getContainerIpAddress() + ":" + mysqlContainer.getPort() + "/batch"
            );

            values.applyTo(configurableApplicationContext);
        }
    }

}

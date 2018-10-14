package gs.msg.target.job;

import gs.msg.target.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class Test {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private SimpleMessageListenerContainer rabbitListener;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get("simpleJob")
                .start(simpleStep1())
                .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is Step1");

                    rabbitTemplate.convertAndSend(Config.MAIL_QUEUE_NAME, "hello11");
                    log.info("12312");

                    rabbitListener.setMessageListener(new MessageListener() {
                        @Override
                        public void onMessage(Message message) {
                            log.info("receiver msg :::>>> " + message);
                        }
                    });

                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}

package gs.msg.target.job;

import gs.msg.target.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessagePostProcessor;
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
    //@Autowired private SimpleMessageListenerContainer rabbitListener;

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

                    String msg = "";
                    for(int i=0; i<100; i++){
                        msg = "test입니다. i=" + i;
                        rabbitTemplate.convertAndSend(Config.MAIL_QUEUE_NAME, (Object) msg ,new MessagePostProcessor() {
                            @Override
                            public Message postProcessMessage(Message message) throws AmqpException {
                                message.getMessageProperties().setPriority(1);
                                message.getMessageProperties().setHeader("schdlId",1234);
                                return message;
                            }
                        });
                    }

//                    rabbitListener.setMessageListener(new MessageListener() {
//                        @Override
//                        public void onMessage(Message message) {
//                            log.info("receiver msg1 :::>>> " + message.getMessageProperties().getHeaders().get("schdlId"));
//                            log.info("receiver msg2 :::>>> " + message);
//                        }
//                    });

                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}

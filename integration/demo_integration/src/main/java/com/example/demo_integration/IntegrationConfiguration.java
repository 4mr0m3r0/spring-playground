package com.example.demo_integration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Configuration
public class IntegrationConfiguration {
    @Bean
    DirectChannel files() {
        return MessageChannels.direct().getObject();
    }

    @RestController
    public static class FileNameRestController {
        private final MessageChannel files;

        @RequestMapping(method = RequestMethod.GET, value = "/files/")
        void triggerJobForFile(@RequestParam String file) {
            Message<File> messageFile = MessageBuilder.withPayload(new File(file)).build();
            this.files.send(messageFile);
        }

        @Autowired
        public FileNameRestController(MessageChannel files) {
            this.files = files;
        }
    }

    // TODO: integrate with batch demo code
//    @Bean
//    public IntegrationFlow batchJob(
//            Job job,
//            JdbcTemplate jdbcTemplate,
//            JobLauncher jobLauncher,
//            MessageChannel files
//    ) {
//        return IntegrationFlow.from(files)
//                .transform((GenericTransformer<File, JobLaunchRequest>) file -> {
//                    JobParameters jobParameters = new JobParametersBuilder().addString("file", file.getAbsolutePath()).toJobParameters();
//                    return new JobLaunchRequest(job, jobParameters);
//                })
//                .handle(new JobLaunchingGateway(jobLauncher))
//                .handle(JobExecution.class, (payload, headers) -> {
//                    System.out.println("Job execution status: " + payload.getExitStatus());
//                    // TODO: copy the rest here, from batch demo code.
//                    return null;
//                })
//                .get();
//    }

    @Bean
    IntegrationFlow incomingFiles(@Value("${HOME}/Desktop/data/in") File dir) {
        return IntegrationFlow.from(
                Files.inboundAdapter(dir).preventDuplicates(true).autoCreateDirectory(true),
                poller -> poller.poller(spec -> spec.fixedRate(1000))
        ).channel(this.files()).get();
    }

//    @Bean
//    IntegrationFlow incomingFiles(@Value("${HOME}/Desktop/data/in") File dir) {
//        return IntegrationFlow.from(
//                Files.inboundAdapter(dir).preventDuplicates(true).autoCreateDirectory(true),
//                poller -> poller.poller(spec -> spec.fixedRate(1000))
//        ).handle(File.class, (payload, headers) -> {
//            System.out.println("Incoming file: " + payload.getAbsolutePath());
//            return null;
//        }).get();
//    }
}

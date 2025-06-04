package com.example.demo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Bean
    @StepScope //The Scope mechanism comes because we may run this job multiple times we have different parameters. We want parameters that are different for each run of the job.
    FlatFileItemReader<Person> reader(@Value("#{jobParameters[file]}") File file) {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(file));
        reader.setLineMapper(new DefaultLineMapper<Person>() {
            {
                this.setLineTokenizer(new DelimitedLineTokenizer(",") {
                    {
                        this.setNames(new String[]{"first", "last", "email"});
                    }
                });
                this.setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
                    {
                        this.setTargetType(Person.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    JdbcBatchItemWriter<Person> writer(DataSource h2) {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(h2);
        writer.setSql("insert into PEOPLE(first, last, email) values (:first, :last, :email)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

    @Bean
    public Step step(
            JobRepository repository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Person> reader,
            JdbcBatchItemWriter<Person> writer
    ) { // TODO: Adapt all these to use Job Repository Caused by: java.lang.IllegalStateException: JobRepository is mandatory
        return new StepBuilder("file-to-database", repository)
                .<Person, Person>chunk(5, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();

    }
    @Bean
    Job personEtl (JobRepository repository, Step step) {
        return new JobBuilder("etl", repository)
                .start(step)
                .build();
    }

    @Bean
    CommandLineRunner commandLineRunner(
            JobLauncher launcher,
            Job job,
            @Value("${file}") File in,
            JdbcTemplate template
    ) {
        return args -> {
            JobExecution execution = launcher.run(job, new JobParametersBuilder()
                    .addString("file", in.getAbsolutePath())
                    .toJobParameters()
            );
            System.out.println("execution status: " + execution.getExitStatus().getExitCode());
            List<Person> persons = template.query("select * from PEOPLE", (rs, rowNum) -> new Person(
                    rs.getString("first"),
                    rs.getString("last"),
                    rs.getString("email")
            ));
            persons.forEach(System.out::println);
        };
    }
}

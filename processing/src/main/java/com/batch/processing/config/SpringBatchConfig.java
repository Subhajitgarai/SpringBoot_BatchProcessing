package com.batch.processing.config;

import com.batch.processing.entity.Customer;
import com.batch.processing.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchConfig {
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    //To Read the File
    @Bean
    public FlatFileItemReader<Customer>reader(){
        FlatFileItemReader<Customer>itemReader=new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("/home/cbnits-94/IdeaProjects/BatchProcessing/processing/src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }
    //ItemProcessor Bean
    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }
    @Bean
    public RepositoryItemWriter<Customer>write(){
        RepositoryItemWriter<Customer>writer=new RepositoryItemWriter<>();
        writer.setRepository(customerRepo);
        writer.setMethodName("save");
        return writer;
    }
    @Bean
    public Step importStep(){
        return new StepBuilder("csvImport",jobRepository)
                .<Customer,Customer>chunk(10,platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(write())
                .build();
    }
    @Bean
    public Job runJob(){
        return new JobBuilder("importCustomers",jobRepository)
                .start(importStep())
                .build();
    }


    //Maps Each line of Csv File to a Customer(Our Entity) Object
    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer>lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        //Converts each line to csv type to Student type
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}

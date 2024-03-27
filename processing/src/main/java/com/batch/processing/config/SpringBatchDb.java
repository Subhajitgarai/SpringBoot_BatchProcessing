package com.batch.processing.config;

import com.batch.processing.entity.Customer;
import com.batch.processing.entity.CustomerDetails;
import com.batch.processing.repository.CustomerDetailsRepo;
import com.batch.processing.repository.CustomerRepo;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
//From One Database to Another Database
@Configuration
public class SpringBatchDb {
    @Autowired
    private CustomerDetailsRepo customerDetailsRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    //To Read the Records from the database
    @Bean
    public JpaPagingItemReader<Customer> reader() {
        JpaPagingItemReader<Customer>jpaPagingItemReader=new JpaPagingItemReader<>();
        jpaPagingItemReader.setName("customerItemReader");
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
        jpaPagingItemReader.setQueryString("SELECT c FROM Customer c");
        jpaPagingItemReader.setPageSize(10);
        return jpaPagingItemReader;
    }
    //ItemProcessor Bean
    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }
    @Bean
    public RepositoryItemWriter<CustomerDetails> write(){
        RepositoryItemWriter<CustomerDetails>writer=new RepositoryItemWriter<>();
        writer.setRepository(customerDetailsRepo);
        writer.setMethodName("save");
        return writer;
    }
    @Bean
    public Step importStep(){
        return new StepBuilder("csvImport",jobRepository)
                .<Customer,CustomerDetails>chunk(10,platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(write())
                .taskExecutor(taskExecutor())
                .build();
    }
    @Bean
    public Job runJob(){
        return new JobBuilder("importCustomers",jobRepository)
                .start(importStep())
                .build();
    }
    //Task Executor for executing the threads in parallel
    @Bean
    public TaskExecutor taskExecutor(){
        SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}

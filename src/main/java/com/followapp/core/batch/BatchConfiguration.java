package com.followapp.core.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;

import com.followapp.core.mappers.CallDetailsRowMapper;
import com.followapp.core.data.CallResultPreparedStatementSetter;
import com.followapp.core.model.CallDetails;
import com.followapp.core.model.CallResult;

@Configuration
@PropertySources({
	@PropertySource("classpath:application-${envTarget:staging}.properties"),
	@PropertySource("${propertiesFile.path}")
})
public class BatchConfiguration {

    @Autowired
    public JdbcTemplate jdbcTemplate;
	
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public IvrProcessor ivrProcessor;
    
    @Autowired
    public CallResultPreparedStatementSetter callResultPss;
    
    public static final String JOB_NAME="reminderJob";
    
    //Param Seq : IVR_CALL_ID , CARE_RECIPIENT_ID, P_prescription_details_id , P_DATE_TIME_CALLED , P_CALL_STATUS
    private static final String UPDATE_CALL_RESULT = "call update_call_status(?, ?, ?,CURDATE(), ?)" ;
    
    //Param Seq: P_DATE_TIME
    private static final String GET_CALL_DETAILS_LIST = "call get_call_details(CURDATE())";
    
    @Bean
    @StepScope
    public JdbcCursorItemReader<CallDetails> reader() {
    	JdbcCursorItemReader<CallDetails> reader = new JdbcCursorItemReader<>();
    	reader.setDataSource(jdbcTemplate.getDataSource());
    	reader.setSql(GET_CALL_DETAILS_LIST);
    	reader.setRowMapper(new CallDetailsRowMapper());
    	return reader;
    }

    @Bean
    public IvrProcessor processor() {
        return ivrProcessor;
    }


    @Bean
    public JdbcBatchItemWriter<CallResult> writer() {
        JdbcBatchItemWriter<CallResult> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter(callResultPss);
        writer.setSql(UPDATE_CALL_RESULT);
        writer.setDataSource(jdbcTemplate.getDataSource());
        writer.setAssertUpdates(false);
        return writer;
    }

    @Bean
    public Job reminderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .flow(reminderStep())
                .end()
                .build();
    }

    @Bean
    public Step reminderStep() {
        return stepBuilderFactory.get("step1")
                .<CallDetails, CallResult> chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}

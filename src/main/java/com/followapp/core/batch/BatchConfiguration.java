package com.followapp.core.batch;

import com.followapp.core.data.ScheduleRunPreparedStatementSetter;
import com.followapp.core.model.ScheduleDetail;
import com.followapp.core.model.ScheduleRun;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Configuration
@PropertySources({
        @PropertySource("classpath:/com/followapp/core/config/application-${envTarget:prod}.properties"),
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
    public ScheduleRunPreparedStatementSetter scheduleRunPreparedStatementSetter;

    public static final String JOB_NAME = "reminderJob";

    //Param Seq : ScheduleId, RecipientId, IvrRequestId, RunDateTime, Status, UpdateDateTime
    private static final String UPDATE_SCHEDULE_RUN = "exec update_schedule_run @schedule_id=?, @recipient_id=?, " +
            "@ivr_request_id=?, @run_date_time=?, @status=?, @update_datetime=?";

    //Param Seq: P_DATE_TIME
    private static final String GET_SCHEDULE_DETAILS_LIST = "exec get_schedule_detail @p_run_date_time=?";

    @Bean
    @StepScope
    public JdbcCursorItemReader<ScheduleDetail> reader() {
        JdbcCursorItemReader<ScheduleDetail> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(jdbcTemplate.getDataSource());
        reader.setSql(GET_SCHEDULE_DETAILS_LIST);
        reader.setPreparedStatementSetter(new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            }
        });
        reader.setRowMapper(new BeanPropertyRowMapper<>(ScheduleDetail.class));
        return reader;
    }

    @Bean
    public IvrProcessor processor() {
        return ivrProcessor;
    }

    @Bean
    public JdbcBatchItemWriter<ScheduleRun> writer() {
        JdbcBatchItemWriter<ScheduleRun> writer = new JdbcBatchItemWriter<>();
        writer.setItemPreparedStatementSetter(scheduleRunPreparedStatementSetter);
        writer.setSql(UPDATE_SCHEDULE_RUN);
        writer.setDataSource(jdbcTemplate.getDataSource());
        writer.setAssertUpdates(false);
        return writer;
    }

    @Bean
    public Job reminderJob() {
        RunIdIncrementer runIdIncrementer = new RunIdIncrementer();
        runIdIncrementer.setKey("time");
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(runIdIncrementer)
                .flow(reminderStep())
                .end()
                .build();
    }

    @Bean
    public Step reminderStep() {
        return stepBuilderFactory.get("step1")
                .<ScheduleDetail, ScheduleRun>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}

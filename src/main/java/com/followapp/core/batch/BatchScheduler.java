package com.followapp.core.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Configuration
@EnableScheduling
public class BatchScheduler {
	
	private static final Logger LOG = LoggerFactory.getLogger(BatchScheduler.class);
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobRegistry jobRegistry;
	
	public static final String JOB_NAME="reminderJob";
	
	//@Scheduled(cron = "0 5 10-19 * * *")
	@Scheduled(cron = "0 ${app.scheduler.batchTriggerTime} 6 * * *")	// Start at 10 AM only
	public void reportCurrentTime() {
		LOG.info("Job Started");
		JobParameters jobParameters = 
				  new JobParametersBuilder()
				  .addLong("time",System.currentTimeMillis()).toJobParameters();
		try {
			Job job = jobRegistry.getJob(JOB_NAME);
			jobLauncher.run(job, jobParameters);
		}
		catch (Exception e) {
			LOG.error("Exception while executing batch job." + e.getMessage(), e);
		}

	}
}

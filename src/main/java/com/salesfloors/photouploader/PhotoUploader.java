package com.salesfloors.photouploader;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class PhotoUploader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
    try {
        JobDetail job = JobBuilder.newJob(UploadFacePicsJob.class)
        .withIdentity("batchJob", "batchGroup")
        .build();
    
        Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("batchTrigger", "batchGroup")
        .startNow()
        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))            
        .build();
    
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    
    } catch (SchedulerException e) {
        e.printStackTrace();
    }
	    
	}
	
	public PhotoUploader() {}
}

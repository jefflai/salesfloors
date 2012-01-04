package org.salesfloors.recognizer;

import java.io.IOException;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.salesfloors.aws.AwsClient;

/**
 * Face recognizer
 *
 */
public class FaceRecognizer 
{
	AwsClient aws;
	
	public FaceRecognizer() throws IOException {
		aws = new AwsClient();
	}
	
    public static void main( String[] args )
    {
    	try {
	        JobDetail job = JobBuilder.newJob(ReadPhotosAndRecognizeFacesJob.class)
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
}

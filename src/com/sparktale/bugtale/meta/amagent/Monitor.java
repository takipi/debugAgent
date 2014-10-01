package com.sparktale.bugtale.meta.amagent;

import com.sparktale.bugtale.meta.amagent.data.Record;
import com.sparktale.bugtale.meta.amagent.data.StackTrace;


public class Monitor
{
	private static volatile Recorder recorder;
	
	public static void init(Recorder recorder)
	{
		Monitor.recorder = recorder;
	}
	
	public static void onAllocation()
	{
		try
		{
			long timestamp = System.currentTimeMillis();
			StackTrace stackTrace = new StackTrace(Thread.currentThread().getStackTrace());
			
			Record record = new Record(timestamp, stackTrace);
			
			synchronized (recorder)
			{
				recorder.record(record);
			}
		}
		catch (Exception e) {}
	}
}

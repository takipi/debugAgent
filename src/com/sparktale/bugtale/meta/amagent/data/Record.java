package com.sparktale.bugtale.meta.amagent.data;

public class Record
{
	private final long timestamp;
	private final StackTrace stackTrace;
	
	public Record(long timestamp, StackTrace stackTrace)
	{
		this.timestamp = timestamp;
		this.stackTrace = stackTrace;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public StackTrace getStackTrace()
	{
		return stackTrace;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		
		if ((o == null) ||
			(o.getClass() != Record.class))
		{
			return false;
		}
		
		Record r = (Record)o;
		
		return ((r.timestamp == this.timestamp) &&
				(r.stackTrace.equals(this.stackTrace)));
	}
	
	@Override
	public int hashCode()
	{
		return Long.valueOf(timestamp).hashCode();
	}
}

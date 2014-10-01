package com.sparktale.bugtale.meta.amagent.analysis.analyzer;

import com.sparktale.bugtale.meta.amagent.data.Record;

public class TimestampFilter extends Analyzer
{
	private final long minTimestamp;
	private final long maxTimestamp;
	
	public TimestampFilter(long minTimestamp, long maxTimestamp, Analyzer... nexts)
	{
		super(nexts);
		
		this.minTimestamp = minTimestamp;
		this.maxTimestamp = maxTimestamp;
	}
	
	@Override
	protected void internalStart()
	{
		// Nothing
	}
	
	@Override
	protected void internalFinish()
	{
		// Nothing
	}
	
	@Override
	protected Record internalProcess(Record record)
	{
		long timestamp = record.getTimestamp();
		
		if ((timestamp < minTimestamp) ||
			(timestamp >= maxTimestamp))
		{
			return null;
		}
		
		return record;
	}
}

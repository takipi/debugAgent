package com.sparktale.bugtale.meta.amagent.analysis.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sparktale.bugtale.meta.amagent.data.Record;

public class TimespanAggregator extends Analyzer
{
	private final long timespan;
	private final Map<Long, Integer> aggregate;
	
	private long curTimebase;
	private int curCount;
	
	public TimespanAggregator(long timespan, Analyzer... nexts)
	{
		super(nexts);
		
		this.timespan = timespan;
		this.aggregate = new HashMap<Long, Integer>();
	}
	
	@Override
	protected void internalStart()
	{
		aggregate.clear();
		
		curTimebase = 0l;
		curCount = 0;
	}
	
	@Override
	protected void internalFinish()
	{
		aggregate.put(curTimebase, curCount);
	}
	
	@Override
	protected Record internalProcess(Record record)
	{
		long timestamp = record.getTimestamp();
		long timebase = timestamp / timespan;
		
		if (timebase < curTimebase)
		{
			throw new IllegalStateException("Is time going backwards?!");
		}
		
		if (timebase > curTimebase)
		{
			aggregate.put(curTimebase, curCount);
			
			curTimebase = timebase;
			curCount = 0;
		}
		
		curCount++;
		
		return record;
	}
	
	@Override
	public String toString()
	{
		return toSimpleString();
	}
	
	public String toSimpleString()
	{
		SortedMap<Long, Integer> sortedMap = new TreeMap<Long, Integer>(aggregate);
		
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Long, Integer> entry : sortedMap.entrySet())
		{
			long timebase = entry.getKey();
			int count = entry.getValue();
			
			long timestamp = timebase * timespan;
			
			sb.append(timestamp).append(" = ").append(count).append("\n");
		}
			
		return sb.toString();
	}
	
	public String toSortedString()
	{
		SortedMap<Integer, List<Long>> sortedMap = new TreeMap<Integer, List<Long>>();
		
		for (Map.Entry<Long, Integer> entry : aggregate.entrySet())
		{
			int key = -entry.getValue();
			long value = entry.getKey();
			
			List<Long> timebases = sortedMap.get(key);
			
			if (timebases == null)
			{
				timebases = new ArrayList<Long>();
				sortedMap.put(key, timebases);
			}
			
			timebases.add(value);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Integer, List<Long>> entry : sortedMap.entrySet())
		{
			int count = -entry.getKey();
			List<Long> timebases = entry.getValue();
			
			List<Long> timestamps = new ArrayList<Long>();
			
			for (long timebase : timebases)
			{
				timestamps.add(timebase * timespan);
			}
			
			sb.append(count).append(" = ").append(timestamps).append("\n");
		}
		
		return sb.toString();
	}
}

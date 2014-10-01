package com.sparktale.bugtale.meta.amagent.analysis.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sparktale.bugtale.meta.amagent.data.Record;
import com.sparktale.bugtale.meta.amagent.data.StackTrace;

public class Histogram extends Analyzer
{
	private final Map<StackTrace, Integer> counts;
	
	public Histogram(Analyzer... nexts)
	{
		super(nexts);
		
		this.counts = new HashMap<StackTrace, Integer>();
	}
	
	@Override
	protected void internalStart()
	{
		counts.clear();
	}
	
	@Override
	protected void internalFinish()
	{
		// Nothing
	}
	
	@Override
	protected Record internalProcess(Record record)
	{
		StackTrace stackTrace = record.getStackTrace();
		
		if (counts.containsKey(stackTrace))
		{
			counts.put(stackTrace, counts.get(stackTrace) + 1);
		}
		else
		{
			counts.put(stackTrace, 1);
		}
		
		return record;
	}
	
	@Override
	public String toString()
	{
		SortedMap<Integer, List<StackTrace>> sortedStacks = new TreeMap<Integer, List<StackTrace>>();
		
		for (Map.Entry<StackTrace, Integer> entry : counts.entrySet())
		{
			int key = -entry.getValue();
			StackTrace value = entry.getKey();
			
			List<StackTrace> stacks = sortedStacks.get(key);
			
			if (stacks == null)
			{
				stacks = new ArrayList<StackTrace>();
				sortedStacks.put(key, stacks);
			}
			
			stacks.add(value);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Integer, List<StackTrace>> entry : sortedStacks.entrySet())
		{
			int count = -entry.getKey();
			List<StackTrace> stackTraces = entry.getValue();
			
			sb.append(count).append(":\n");
			
			for (StackTrace stackTrace : stackTraces)
			{
				sb.append(stackTrace).append("\n");
			}
			
			sb.append("\n");
		}
		
		return sb.toString();
	}
}

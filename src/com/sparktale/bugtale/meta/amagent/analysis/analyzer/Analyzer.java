package com.sparktale.bugtale.meta.amagent.analysis.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sparktale.bugtale.meta.amagent.data.Record;

public abstract class Analyzer
{
	private final List<Analyzer> nexts;
	
	protected Analyzer(Analyzer... nexts)
	{
		this.nexts = new ArrayList<Analyzer>(nexts.length);
		
		for (Analyzer next : nexts)
		{
			this.nexts.add(next);
		}
	}
	
	public void start()
	{
		internalStart();
		
		for (Analyzer next : nexts)
		{
			next.start();
		}
	}
	
	public void finish()
	{
		internalFinish();
		
		for (Analyzer next : nexts)
		{
			next.finish();
		}
	}
	
	public void process(Record record)
	{
		Record result = internalProcess(record);
		
		if (result != null)
		{
			for (Analyzer next : nexts)
			{
				next.process(result);
			}
		}
	}
	
	protected abstract void internalStart();
	protected abstract void internalFinish();
	
	protected abstract Record internalProcess(Record record);
}

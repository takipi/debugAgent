package com.sparktale.bugtale.meta.amagent.analysis.analyzer;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import com.sparktale.bugtale.meta.amagent.data.Record;
import com.sparktale.bugtale.meta.amagent.data.StackTrace;

public class Printer extends Analyzer
{
	private final PrintWriter pw;
	
	public Printer(OutputStream outputStream, Analyzer... nexts)
	{
		super(nexts);
		
		this.pw = new PrintWriter(outputStream);
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
		StackTrace stackTrace = record.getStackTrace();
		
		pw.println(new Date(timestamp));
		pw.println(stackTrace);
		pw.println();
		pw.flush();
		
		return record;
	}
}

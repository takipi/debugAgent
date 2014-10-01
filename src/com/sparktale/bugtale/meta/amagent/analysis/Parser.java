package com.sparktale.bugtale.meta.amagent.analysis;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sparktale.bugtale.meta.amagent.analysis.analyzer.Analyzer;
import com.sparktale.bugtale.meta.amagent.data.Record;
import com.sparktale.bugtale.meta.amagent.data.StackTrace;

public class Parser
{
	private final DataInputStream dis;
	private final Map<Integer, StackTrace> stacks;
	
	public Parser(String inputFileName) throws IOException
	{
		FileInputStream fis = new FileInputStream(inputFileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		this.dis = new DataInputStream(bis);
		this.stacks = new HashMap<Integer, StackTrace>();
	}
	
	public void parse(Analyzer next) throws IOException
	{
		if (next != null)
		{
			next.start();
		}
		
		Record record;
		
		while ((record = parseRecord()) != null)
		{
			if (next != null)
			{
				next.process(record);
			}
		}
		
		if (next != null)
		{
			next.finish();
		}
	}
	
	private Record parseRecord() throws IOException
	{
		if (dis.available() == 0)
		{
			return null;
		}
		
		long timestamp = dis.readLong();
		int stackHash = dis.readInt();
		
		StackTrace stackTrace;
		
		if (dis.readBoolean())
		{
			int stackLength = dis.readInt();
			
			StackTraceElement[] stackElements = new StackTraceElement[stackLength];
			
			for (int i = 0; i < stackLength; i++)
			{
				String className = dis.readUTF();
				String methodName = dis.readUTF();
				String fileName = dis.readUTF();
				int lineNumber = dis.readInt();
				
				stackElements[i] = new StackTraceElement(className, methodName, fileName, lineNumber);
			}
			
			stackTrace = new StackTrace(stackElements);
			
			stacks.put(stackHash, stackTrace);
		}
		else
		{
			stackTrace = stacks.get(stackHash);
		}
		
		return new Record(timestamp, stackTrace);
	}
}

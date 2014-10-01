package com.sparktale.bugtale.meta.amagent;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.sparktale.bugtale.meta.amagent.data.Record;
import com.sparktale.bugtale.meta.amagent.data.StackTrace;

public class Recorder
{
	private final DataOutputStream dos;
	private final Set<Integer> hashes;
	
	public Recorder(String outputFileName) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(outputFileName);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		
		this.dos = new DataOutputStream(bos);
		this.hashes = new HashSet<Integer>();
	}
	
	public void record(Record record) throws IOException
	{
		long timestamp = record.getTimestamp();
		StackTrace stackTrace = record.getStackTrace();
		
		int stackHash = stackTrace.getStackHash();
		
		dos.writeLong(timestamp);
		dos.writeInt(stackHash);
		
		if (hashes.add(stackHash))
		{
			StackTraceElement[] stackElements = stackTrace.getElements();
			
			dos.writeBoolean(true);
			dos.writeInt(stackElements.length);
			
			for (StackTraceElement stackElement : stackElements)
			{
				String className = stackElement.getClassName();
				String methodName = stackElement.getMethodName();
				String fileName = stackElement.getFileName();
				int lineNumber = stackElement.getLineNumber();
				
				dos.writeUTF(className);
				dos.writeUTF(methodName);
				dos.writeUTF((fileName != null) ? fileName : "");
				dos.writeInt(lineNumber);
			}
		}
		else
		{
			dos.writeBoolean(false);
		}
		
		dos.flush();
	}
}

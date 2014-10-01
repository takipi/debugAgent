package com.sparktale.bugtale.meta.amagent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Agent
{
	public static void premain(String agentArgs, Instrumentation inst)
	{
		try
		{
			internalPremain(agentArgs, inst);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void internalPremain(String agentArgs, Instrumentation inst) throws IOException
	{
		System.out.println("Takipi allocation monitor agent loaded.");
		
		Options options = Options.parse(agentArgs);
		
		String targetClassName = options.getTargetClassName();
		String outputFilePrefix = options.getOutputFilePrefix();
		
		String outputFileName = outputFilePrefix + "." + Long.toString(System.currentTimeMillis());
		
		System.out.println("  Target class name: " + targetClassName);
		System.out.println("  Output file name:  " + outputFileName);
		
		Transformer transformer = new Transformer(targetClassName);
		Recorder recorder = new Recorder(outputFileName);
		
		Monitor.init(recorder);
		
		inst.addTransformer(transformer, true);
	}
}

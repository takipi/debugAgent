package com.sparktale.bugtale.meta.amagent;

import java.util.HashMap;
import java.util.Map;

public class Options
{
	private static final String TARGET_CLASS_NAME_KEY	= "c";
	private static final String OUTPUT_FILE_PREFIX_KEY	= "o";
	
	private final String targetClassName;
	private final String outputFilePrefix;
	
	public static Options parse(String optionsStr)
	{
		Map<String, String> optionMap = toMap(optionsStr);
		
		String targetClassName = optionMap.get(TARGET_CLASS_NAME_KEY);
		String outputFilePrefix = optionMap.get(OUTPUT_FILE_PREFIX_KEY);
		
		return new Options(targetClassName, outputFilePrefix);
	}
	
	private Options(String targetClassName, String outputFilePrefix)
	{
		this.targetClassName = targetClassName;
		this.outputFilePrefix = outputFilePrefix;
	}
	
	public String getTargetClassName()
	{
		return targetClassName;
	}
	
	public String getOutputFilePrefix()
	{
		return outputFilePrefix;
	}
	
	private static Map<String, String> toMap(String optionsStr)
	{
		Map<String, String> optionMap = new HashMap<String, String>();
		
		String[] pairs = optionsStr.split(",");
		
		for (String pair : pairs)
		{
			int splitIndex = pair.indexOf("=");
			
			String key = pair.substring(0, splitIndex);
			String value = pair.substring(splitIndex + 1);
			
			optionMap.put(key, value);
		}
		
		return optionMap;
	}
}

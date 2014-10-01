package com.sparktale.bugtale.meta.amagent.data;

import java.util.Arrays;

public class StackTrace
{
	private final StackTraceElement[] elements;
	
	public StackTrace(StackTraceElement[] elements)
	{
		this.elements = elements;
	}
	
	public StackTraceElement[] getElements()
	{
		return elements;
	}
	
	public int getStackHash()
	{
		return toString().hashCode(); // Backwards compatible
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		
		if ((o == null) ||
			(o.getClass() != StackTrace.class))
		{
			return false;
		}
		
		StackTrace st = (StackTrace)o;
		
		return Arrays.equals(this.elements, st.elements);
	}
	
	@Override
	public int hashCode()
	{
		return Arrays.hashCode(elements);
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for (int i = 1; i < elements.length; i++)
		{
			builder.append("\t");
			builder.append(elements[i]);
			
			if (i < elements.length - 1)
			{
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
}

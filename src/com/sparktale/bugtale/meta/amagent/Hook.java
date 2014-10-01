package com.sparktale.bugtale.meta.amagent;

import com.sparktale.bugtale.meta.amagent.asm.Type;

public class Hook
{
	public static final String HOOK_OWNER_NAME = Type.getInternalName(Hook.class);
	public static final String HOOK_METHOD_NAME	= Hook.class.getDeclaredMethods()[0].getName();
	public static final String HOOK_METHOD_DESC	= Type.getMethodDescriptor(Hook.class.getDeclaredMethods()[0]);
	
	public static void onAllocation()
	{
		Monitor.onAllocation();
	}
}

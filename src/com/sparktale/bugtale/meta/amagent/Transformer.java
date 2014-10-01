package com.sparktale.bugtale.meta.amagent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.sparktale.bugtale.meta.amagent.asm.ClassReader;
import com.sparktale.bugtale.meta.amagent.asm.ClassVisitor;
import com.sparktale.bugtale.meta.amagent.asm.ClassWriter;
import com.sparktale.bugtale.meta.amagent.asm.MethodVisitor;
import com.sparktale.bugtale.meta.amagent.asm.Opcodes;

public class Transformer implements ClassFileTransformer
{
	private static final String INIT_METHOD_NAME	= "<init>";
	
	private final String targetClassName;
	
	public Transformer(String targetClassName)
	{
		this.targetClassName = targetClassName;
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer)
			throws IllegalClassFormatException
	{
		if (!className.equals(targetClassName))
		{
			return null;
		}
		
		ClassReader cr = new ClassReader(classfileBuffer);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		
		AllocationMonitorClassVisitor cv = new AllocationMonitorClassVisitor(cw);
		
		cr.accept(cv, 0);
		
		return cw.toByteArray();
	}
	
	private static class AllocationMonitorClassVisitor extends ClassVisitor
	{
		public AllocationMonitorClassVisitor(ClassVisitor cv)
		{
			super(Opcodes.ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
		{
			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			
			if ((mv == null) ||
				(!name.equals(INIT_METHOD_NAME)))
			{
				return mv;
			}
			
			return new AllocationMonitorCtorVisitor(mv);
		}
	}
	
	private static class AllocationMonitorCtorVisitor extends MethodVisitor
	{
		public AllocationMonitorCtorVisitor(MethodVisitor mv)
		{
			super(Opcodes.ASM5, mv);
		}
		
		@Override
		public void visitCode()
		{
			super.visitCode();
			
			super.visitMethodInsn(Opcodes.INVOKESTATIC,
					Hook.HOOK_OWNER_NAME,
					Hook.HOOK_METHOD_NAME,
					Hook.HOOK_METHOD_DESC, false);
		}
	}
}

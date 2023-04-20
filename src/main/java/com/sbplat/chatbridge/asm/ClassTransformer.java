package com.sbplat.chatbridge.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import com.sbplat.chatbridge.events.ClientChatEvent;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (!transformedName.equals("net.minecraft.client.gui.GuiScreen")) {
        // if (!transformedName.equals("net.minecraft.client.entity.EntityPlayerSP")) {
            return bytes;
        }

        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            // Make sure it's not the sendChatMessage method wrapper.
            if (!method.desc.equals("(Ljava/lang/String;Z)V")) {
                continue;
            }
            String mappedMethodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);
            if (!mappedMethodName.equals("sendChatMessage") && !mappedMethodName.equals("func_175281_b")) {
            // if (!mappedMethodName.equals("sendChatMessage") && !mappedMethodName.equals("func_71165_d")) {
                continue;
            }
            // Inject the following code:
            /*
            if (MinecraftForge.EVENT_BUS.post(new ClientChatEvent(message))) {
                return;
            }
            */
            String minecraftForgeClassName = Type.getInternalName(MinecraftForge.class);
            String eventBusClassName = Type.getInternalName(EventBus.class);
            String eventClassName = Type.getInternalName(ClientChatEvent.class);
            InsnList toInject = new InsnList();
            // MinecraftForge.EVENT_BUS
            toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, minecraftForgeClassName, "EVENT_BUS", Type.getDescriptor(EventBus.class)));
            // new ClientChatEvent(message)
            toInject.add(new TypeInsnNode(Opcodes.NEW, eventClassName));
            toInject.add(new InsnNode(Opcodes.DUP));
            toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            // ClientChatEvent(message)
            toInject.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, eventClassName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)), false));
            // MinecraftForge.EVENT_BUS.post(new ClientChatEvent(message))
            toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, eventBusClassName, "post", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(Event.class)), false));
            // if (MinecraftForge.EVENT_BUS.post(new ClientChatEvent(message))) { return; }
            LabelNode label = new LabelNode();
            toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
            toInject.add(new InsnNode(Opcodes.RETURN));
            toInject.add(label);
            // Inject at the beginning.
            method.instructions.insertBefore(method.instructions.getFirst(), toInject);
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

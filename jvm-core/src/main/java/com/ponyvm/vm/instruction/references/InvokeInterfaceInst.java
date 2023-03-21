package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Class;
import com.ponyvm.vm.rtda.heap.Method;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.rtda.heap.NativeMethod;
import com.ponyvm.vm.util.Utils;

public class InvokeInterfaceInst implements Instruction {

  public final String clazzName;
  public final String methodName;
  public final String methodDescriptor;

  public final int count;
  public final int zero;

  public InvokeInterfaceInst(String clazzName, String methodName, String methodDescriptor, int count, int zero) {
    this.clazzName = clazzName;
    this.methodName = methodName;
    this.methodDescriptor = methodDescriptor;
    this.count = count;
    this.zero = zero;
  }

  @Override
  public int offset() {
    return 5;
  }

  @Override
  public void execute(Frame frame) {
    NativeMethod nm = Heap.findMethod(Utils.genNativeMethodKey( clazzName, methodName, methodDescriptor));
    if (nm != null) {
      nm.invoke(frame);
      return;
    }

    Class clazz = Heap.findClass(this.clazzName);
    if (clazz == null) {
      clazz = frame.method.clazz.classLoader.loadClass(clazzName);
    }

    Utils.clinit(clazz);

    Method method = clazz.getMethod(methodName, methodDescriptor);

    if (method == null) {
      // try find interfaces
      if (clazz.interfaceNames.isEmpty()) {
        throw new IllegalStateException();
      }

      // already load interface
      if (clazz.getInterfaces().isEmpty()) {
        clazz.interfaceInit(frame);
      }
      if (!clazz.getInterfaces().isEmpty()) {
        for (Class intClass : clazz.getInterfaces()) {
          method= intClass.getMethod(methodName, methodDescriptor);
          if (method != null) {
            break;
          }
        }
      }
    }

    if (method == null) {
      throw new IllegalStateException();
    }

    if (method.isNative()) {
      throw new IllegalStateException("un impl native method call, " + method);
    }

    final Instance ref = frame.getThis(method.getArgSlotSize());
    Method implMethod = ref.clazz.getMethod(methodName, methodDescriptor);
    // method is default method
    if (implMethod == null) {
      implMethod = method;
    }
    Utils.invokeMethod(implMethod);
  }

  @Override
  public String format() {
    return "invokeinterface " + clazzName + " " + methodName + " " + methodDescriptor;
  }

}


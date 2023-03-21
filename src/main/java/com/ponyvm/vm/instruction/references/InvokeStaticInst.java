package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Method;
import com.ponyvm.vm.jmm.heap.NativeMethod;

import com.ponyvm.vm.util.Utils;

public class InvokeStaticInst implements Instruction {

  public final String clazzName;
  public final String methodName;
  public final String descriptor;

  public InvokeStaticInst(String clazzName, String methodName, String descriptor) {
    this.clazzName = clazzName;
    this.methodName = methodName;
    this.descriptor = descriptor;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void execute(Frame frame) {
    NativeMethod nm = Heap.findMethod(Utils.genNativeMethodKey( clazzName, methodName, descriptor));
    if (nm != null) {
      nm.invoke(frame);
      return;
    }

    Class aClass = Heap.findClass(clazzName);
    if (aClass == null) {
      aClass = frame.method.clazz.classLoader.loadClass(clazzName);
    }
    Utils.clinit(aClass);

    Method method = aClass.getMethod(methodName, descriptor);

    if (method.isNative()) {
      throw new IllegalStateException("un impl native method call, " + method);
    }

    Utils.invokeMethod(method);
  }

  @Override
  public String format() {
    return "invokestatic " + clazzName + " " + methodName + " " + descriptor;
  }

  @Override
  public String toString() {
    return "InvokeStaticInst{" +
        "clazzName='" + clazzName + '\'' +
        ", methodName='" + methodName + '\'' +
        ", descriptor='" + descriptor + '\'' +
        '}';
  }
}


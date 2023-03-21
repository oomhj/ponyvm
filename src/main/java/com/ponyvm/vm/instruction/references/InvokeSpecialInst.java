package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Method;
import com.ponyvm.vm.jmm.heap.NativeMethod;
import com.ponyvm.vm.util.Utils;
import java.util.List;

public class InvokeSpecialInst implements Instruction {

  public final String clazz;
  public final String methodName;
  public final String methodDescriptor;

  public InvokeSpecialInst(String clazz, String methodName, String methodDescriptor) {
    this.clazz = clazz;
    this.methodName = methodName;
    this.methodDescriptor = methodDescriptor;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void execute(Frame frame) {
    NativeMethod nm = Heap.findMethod(Utils.genNativeMethodKey(clazz, methodName, methodDescriptor));
    if (nm != null) {
      nm.invoke(frame);
      return;
    }

    Class aClass = Heap.findClass(clazz);
    if (aClass == null) {
      throw new IllegalStateException();
    }

    Method method = aClass.getMethod(methodName, methodDescriptor);
    if (method == null) {
      System.out.println(Utils.genNativeMethodKey(clazz, methodName, methodDescriptor));
      throw new IllegalStateException();
    }

    if (method.isNative()) {
      throw new IllegalStateException("un impl native method call, " + method);
    }

    Utils.invokeMethod(method);
  }

  @Override
  public String format() {
    return "invokespecail " + clazz + " " + methodName + " " + methodDescriptor;
  }
}

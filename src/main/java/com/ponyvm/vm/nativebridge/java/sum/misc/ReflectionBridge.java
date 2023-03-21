package com.ponyvm.vm.nativebridge.java.sum.misc;

import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Instance;

public abstract class ReflectionBridge {

  public static void registerNatives0() {
    Heap.registerMethod("sun/reflect/Reflection_getCallerClass_()Ljava/lang/Class;", frame -> {
      Frame callerFrame = frame.thread.callerFrame();
      Object cls = callerFrame.method.clazz.getRuntimeClass();
      frame.pushRef((Instance) cls);
    });
  }
}

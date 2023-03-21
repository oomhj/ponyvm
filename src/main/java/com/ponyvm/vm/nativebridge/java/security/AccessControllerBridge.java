package com.ponyvm.vm.nativebridge.java.security;

import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Method;
import com.ponyvm.vm.rtda.heap.Instance;

public abstract class AccessControllerBridge {

  public static void registerNative0() {
    // static
    Heap.registerMethod("java/security/AccessController_doPrivileged_(Ljava/security/PrivilegedAction;)Ljava/lang/Object;", frame -> {
      Instance thisObj = (Instance) frame.popRef();
      Method method = thisObj.clazz.getMethod("run", "()Ljava/lang/Object;");
      Frame newFrame = new Frame(method);
      newFrame.setRef(0, thisObj);
      frame.thread.pushFrame(newFrame);
    });
    // static
    Heap.registerMethod("java/security/AccessController_doPrivileged_(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;", frame -> {
      Instance thisObj = (Instance) frame.popRef();
      Method method = thisObj.clazz.getMethod("run", "()Ljava/lang/Object;");
      Frame newFrame = new Frame(method);
      newFrame.setRef(0, thisObj);
      frame.thread.pushFrame(newFrame);
    });
  }
}

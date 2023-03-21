package com.ponyvm.vm.nativebridge.java.util.concurrent;

import com.ponyvm.vm.rtda.heap.Heap;

public abstract class AtomicLongBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/util/concurrent/atomic/AtomicLong_VMSupportsCS8_()Z", frame -> {
      frame.pushInt(0);
    });
    Heap.registerMethod("java/util/concurrent/atomic/AtomicLong_<clinit>_()V", frame -> {
    });
  }
}

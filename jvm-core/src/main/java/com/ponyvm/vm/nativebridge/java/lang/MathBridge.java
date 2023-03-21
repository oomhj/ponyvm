package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.heap.Heap;

public abstract class MathBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/Math_min_(II)I", frame -> {
      Integer v2 = frame.popInt();
      Integer v1 = frame.popInt();
      if (v1 <= v2) {
        frame.pushInt(v1);
      } else {
        frame.pushInt(v2);
      }
    });
    Heap.registerMethod("java/lang/Math_max_(II)I", frame -> {
      Integer v2 = frame.popInt();
      Integer v1 = frame.popInt();
      if (v1 >= v2) {
        frame.pushInt(v1);
      } else {
        frame.pushInt(v2);
      }
    });
  }
}

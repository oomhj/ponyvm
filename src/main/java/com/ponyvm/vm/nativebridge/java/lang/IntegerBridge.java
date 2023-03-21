package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.UnionSlot;
import com.ponyvm.vm.rtda.heap.Class;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Instance;

public abstract class IntegerBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/Integer_valueOf_(I)Ljava/lang/Integer;", frame -> {
      Class clazz = Heap.findClass("java/lang/Integer");
      Instance instance = clazz.newInstance();
      final Integer val = frame.popInt();
      instance.setField("value", "I", UnionSlot.of(val));
      frame.pushRef(instance);
    });
  }
}

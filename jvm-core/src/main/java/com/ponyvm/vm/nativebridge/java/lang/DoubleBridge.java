package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.Slot;
import com.ponyvm.vm.rtda.UnionSlot;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Class;
import com.ponyvm.vm.rtda.heap.Instance;

public abstract class DoubleBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/Double_doubleToRawLongBits_(D)J", frame -> {
      java.lang.Double tmp = frame.popDouble();
      long v = java.lang.Double.doubleToRawLongBits(tmp);
      frame.pushLong(v);
    });
    Heap.registerMethod("java/lang/Double_longBitsToDouble_(J)D", frame -> {
      Long tmp = frame.popLong();
      double v = java.lang.Double.longBitsToDouble(tmp);
      frame.pushDouble(v);
    });

    Heap.registerMethod("java/lang/Double_valueOf_(D)Ljava/lang/Double;", frame -> {
      Class clazz = Heap.findClass("java/lang/Double");
      Instance instance = clazz.newInstance();
      Slot v2 = frame.popSlot();
      Slot v1 = frame.popSlot();
      instance.setField("value", "D", UnionSlot.of(v1, v2));
      frame.pushRef(instance);
    });
  }
}

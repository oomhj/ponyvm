package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.UnionSlot;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Field;
import com.ponyvm.vm.rtda.heap.Instance;

public abstract class ExceptionBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/Exception_<init>_(Ljava/lang/String;)V", frame -> {
      Instance str = (Instance) frame.popRef();
      Instance thisObj = (Instance) frame.popRef();
      Field msgField = thisObj.getField("detailMessage", "Ljava/lang/String;");
      msgField.val = UnionSlot.of(str);
    });
  }
}

package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;
import com.ponyvm.vm.util.Utils;

public abstract class StringBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/String_intern_()Ljava/lang/String;", frame -> {
    });

    Heap.registerMethod("java/lang/String_getBytes_()[B", frame -> {
      Instance obj = frame.popRef();
      String str = Utils.obj2Str(obj);
      byte[] bytes = str.getBytes();

      final PrimitiveArray array = PrimitiveArray.byteArray(bytes.length);
      for (int i = 0; i < bytes.length; i++) {
        array.ints[i] = bytes[i];
      }
      frame.pushRef(array);
    });
  }
}

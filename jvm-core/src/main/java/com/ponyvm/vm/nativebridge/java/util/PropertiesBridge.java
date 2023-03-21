package com.ponyvm.vm.nativebridge.java.util;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.util.Utils;

public abstract class PropertiesBridge {

  public static void registerNative0() {

    // mock
    Heap.registerMethod("java/util/Properties_<init>_()V", frame -> {
      frame.popRef();
    });
    // mock
    Heap.registerMethod("java/util/Properties_getProperty_(Ljava/lang/String;)Ljava/lang/String;", frame -> {
      Instance nameObj = (Instance) frame.popRef();
      frame.popRef();

      String val = System.getProperty(Utils.obj2Str(nameObj));
      if (val == null) {
        frame.pushRef(null);
        return;
      }
      frame.pushRef(Utils.str2Obj(val, frame.method.clazz.classLoader));
    });
  }
}

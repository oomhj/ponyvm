package com.ponyvm.vm.nativebridge.java.lang;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.InstanceArray;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;
import com.ponyvm.vm.util.Utils;

public abstract class SystemBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/lang/System_registerNatives_()V", (frame) -> {
    });

    Heap.registerMethod("java/lang/System_setIn0_(Ljava/io/InputStream;)V", (frame) -> {
    });
    Heap.registerMethod("java/lang/System_setOut0_(Ljava/io/PrintStream;)V", (frame) -> {
    });
    Heap.registerMethod("java/lang/System_setErr0_(Ljava/io/PrintStream;)V", (frame) -> {
    });
    Heap.registerMethod("java/lang/System_currentTimeMillis_()J", (frame) -> frame.pushLong(java.lang.System.currentTimeMillis()));
    Heap.registerMethod("java/lang/System_nanoTime_()J", (frame) -> frame.pushLong(java.lang.System.nanoTime()));
    Heap.registerMethod("java/lang/System_arraycopy_(Ljava/lang/Object;ILjava/lang/Object;II)V", (frame) -> {
      int len = frame.popInt();
      int dsp = frame.popInt();
      final Instance dest = frame.popRef();
      if (dest instanceof InstanceArray) {
        final InstanceArray da = (InstanceArray) dest;
        int ssp = frame.popInt();
        final InstanceArray sa = (InstanceArray) frame.popRef();
        for (int i = 0; i < len; i++) {
          da.items[dsp++] = sa.items[ssp++];
        }
      } else {
        final PrimitiveArray da = (PrimitiveArray) dest;
        int ssp = frame.popInt();
        final PrimitiveArray sa = (PrimitiveArray) frame.popRef();
        for (int i = 0; i < len; i++) {
          if (da.ints != null) {
            da.ints[dsp++] = sa.ints[ssp++];
          } else if (da.longs != null) {
            da.longs[dsp++] = sa.longs[ssp++];
          } else if (da.floats != null) {
            da.floats[dsp++] = sa.floats[ssp++];
          } else {
            da.doubles[dsp++] = sa.doubles[ssp++];
          }
        }
      }
    });
    Heap.registerMethod("java/lang/System_identityHashCode_(Ljava/lang/Object;)I", (frame) -> frame.pushInt(frame.popRef().hashCode()));
    Heap.registerMethod("java/lang/System_initProperties_(Ljava/util/Properties;)Ljava/util/Properties;", (frame) -> {
    });
    Heap.registerMethod("java/lang/System_mapLibraryName_(Ljava/lang/String;)Ljava/lang/String;", (frame) -> {
    });

    // hack
    Heap.registerMethod("java/lang/System_getenv_(Ljava/lang/String;)Ljava/lang/String;", frame -> {
      Instance nameObj = (Instance) frame.popRef();

      String val = System.getenv(Utils.obj2Str(nameObj));
      if (val == null) {
        frame.pushRef(null);
        return;
      }
      frame.pushRef(Utils.str2Obj(val, frame.method.clazz.classLoader));
    });
  }
}

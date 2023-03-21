package com.ponyvm.vm.jmm;

import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.NativeMethod;

public class MetaSpace {

  public static java.lang.Thread main;

  public static java.lang.Thread getMainEnv() {
    return main;
  }

  public static NativeMethod findNativeMethod(String key) {
    return Heap.findMethod(key);
  }
}

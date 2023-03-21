package com.ponyvm.vm.nativebridge.java.io;

import com.ponyvm.vm.rtda.heap.Heap;

public abstract class FileDescriptorBridge {

  public static void registerNative0() {
    Heap.registerMethod("java/io/FileDescriptor_initIDs_()V", frame -> {
    });
  }
}

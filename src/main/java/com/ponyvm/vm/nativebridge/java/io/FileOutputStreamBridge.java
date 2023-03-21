package com.ponyvm.vm.nativebridge.java.io;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Field;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class FileOutputStreamBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/io/FileOutputStream_open0_(Ljava/lang/String;Z)V", frame -> {
    });

    Heap.registerMethod("java/io/FileOutputStream_initIDs_()V", frame -> {
    });

    Heap.registerMethod("java/io/FileOutputStream_close0_()V", frame -> {
      // TODO real close
      frame.popRef();
    });

    Heap.registerMethod("java/io/FileDescriptor_sync_()V", frame -> {
      frame.popRef();
    });

    Heap.registerMethod("java/io/FileOutputStream_write_(IZ)V", frame -> {
      boolean append = frame.popInt() == 1;
      Integer val = frame.popInt();
      Instance thisObj = (Instance) frame.popRef();
      Field fd = thisObj.getField("fd", "Ljava/io/FileDescriptor;");
      Instance fdObj = (Instance) fd.val.getRef();
      int realFd = fdObj.getField("fd", "I").val.getInt();
      // out
      if (realFd == 1) {
        try {
          new FileOutputStream(FileDescriptor.out).write(val);
        } catch (IOException e) {
          throw new IllegalStateException();
        }
      }
      if (realFd == 2) {
        try {
          new FileOutputStream(FileDescriptor.err).write(val);
        } catch (IOException e) {
          throw new IllegalStateException();
        }
      }
    });

    Heap.registerMethod("java/io/FileOutputStream_writeBytes_([BIIZ)V", frame -> {
      boolean append = frame.popInt() == 1;
      int len= frame.popInt();
      int off= frame.popInt();
      PrimitiveArray arg1 = (PrimitiveArray) frame.popRef();

      byte[] bytes = new byte[len];
      for (int i = off; i < len; i++) {
        bytes[i - off] = (byte) arg1.ints[i];
      }

      Instance thisObj = (Instance) frame.popRef();
      Field fd = thisObj.getField("fd", "Ljava/io/FileDescriptor;");
      Instance fdObj = (Instance) fd.val.getRef();
      int realFd = fdObj.getField("fd", "I").val.getInt();
      // out
      if (realFd == 1) {
        try {
          new FileOutputStream(FileDescriptor.out).write(bytes);
        } catch (IOException e) {
          throw new IllegalStateException();
        }
      }
      if (realFd == 2) {
        try {
          new FileOutputStream(FileDescriptor.err).write(bytes);
        } catch (IOException e) {
          throw new IllegalStateException();
        }
      }
    });
  }
}

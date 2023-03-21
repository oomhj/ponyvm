package com.ponyvm.vm.nativebridge.java.io;

import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.InstanceArray;
import com.ponyvm.vm.rtda.heap.Class;
import com.ponyvm.vm.rtda.heap.Field;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.util.Utils;

import java.io.File;

public abstract class UnixFileSystemBridge {

  public static void registerNatives0() {
    Heap.registerMethod("java/io/UnixFileSystem_initIDs_()V", frame -> {
    });

    Heap.registerMethod("java/io/UnixFileSystem_getBooleanAttributes0_(Ljava/io/File;)I", frame -> {
      Instance fileObj = (Instance) frame.popRef();
      Object thisObj = frame.popRef();

      Instance pathObj = (Instance) fileObj.getField("path", "Ljava/lang/String;").val.getRef();
      String path = Utils.obj2Str(pathObj);
      File file = new File(path);
      boolean exists = file.exists();
      boolean directory = file.isDirectory();

      int ret = 0;
      if (exists) {
        ret |= 0x01;
      }
      if (directory) {
        ret |= 0x04;
      }
      frame.pushInt(ret);
    });
    Heap.registerMethod("java/io/UnixFileSystem_canonicalize0_(Ljava/lang/String;)Ljava/lang/String;", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_checkAccess_(Ljava/io/File;I)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_getLastModifiedTime_(Ljava/io/File;)J", frame -> {
      Instance file = (Instance) frame.popRef();
      frame.popRef();
      Field path = file.getField("path", "Ljava/lang/String;");
      String pathStr = Utils.obj2Str(((Instance) path.val.getRef()));
      long lm = new File(pathStr).lastModified();
      frame.pushLong(lm);
    });
    Heap.registerMethod("java/io/UnixFileSystem_getLength_(Ljava/io/File;)J", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_setPermission_(Ljava/io/File;IZZ)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_createFileExclusively_(Ljava/lang/String;)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_delete0_(Ljava/io/File;)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_list_(Ljava/io/File;)[Ljava/lang/String;", frame -> {
      Instance file = (Instance) frame.popRef();
      frame.popRef();
      Field path = file.getField("path", "Ljava/lang/String;");
      String pathStr = Utils.obj2Str(((Instance) path.val.getRef()));
      String[] list = new File(pathStr).list();

      Instance[] items = new Instance[list.length];
      for (int i = 0; i < list.length; i++) {
        items[i] = Utils.str2Obj(list[i], frame.method.clazz.classLoader);
      }

      String name = "[Ljava/lang/String;";
      Class clazz = Heap.findClass(name);
      if (clazz == null) {
        clazz = new Class(1, name, frame.method.clazz.classLoader, null);
        clazz.setSuperClass(Heap.findClass("java/lang/Object"));
        clazz.setStat(2);
        Heap.registerClass(name, clazz);
      }

      InstanceArray arr = new InstanceArray(clazz, items);
      frame.pushRef(arr);
    });
    Heap.registerMethod("java/io/UnixFileSystem_createDirectory_(Ljava/io/File;)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_rename0_(Ljava/io/File;Ljava/io/File;)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_setLastModifiedTime_(Ljava/io/File;J)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_setReadOnly_(Ljava/io/File;)Z", frame -> {
      throw new UnsupportedOperationException();
    });
    Heap.registerMethod("java/io/UnixFileSystem_getSpace_(Ljava/io/File;I)J", frame -> {
      throw new UnsupportedOperationException();
    });
    // hack
    Heap.registerMethod("java/io/UnixFileSystem_normalize_(Ljava/lang/String;II)Ljava/lang/String;", frame -> {
      frame.popInt();
      frame.popInt();
    });
  }
}

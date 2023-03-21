package com.ponyvm.vm.jmm.heap;

import com.ponyvm.vm.util.EnvHolder;
import com.ponyvm.vm.util.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jvm heap
 */
public abstract class Heap {

  private static final Map<String, NativeMethod> NATIVE_METHOD_MAP;
  private static final Map<String, java.lang.Class> STRING_K_CLASS_MAP;

  static {
    NATIVE_METHOD_MAP = new HashMap<>();
    STRING_K_CLASS_MAP = new HashMap<>();
  }

  public static void registerMethod(String key, NativeMethod method) {
    if (NATIVE_METHOD_MAP.containsKey(key)) {
      throw new IllegalStateException();
    }
    NATIVE_METHOD_MAP.put(key, method);
  }

  public static NativeMethod findMethod(String key) {
    return NATIVE_METHOD_MAP.get(key);
  }

  public static java.lang.Class findClass(String name) {
    return STRING_K_CLASS_MAP.get(name);
  }

  public static void registerClass(String name, java.lang.Class clazz) {
    if (EnvHolder.verboseClass) {
      String source = clazz.classLoader.getName();
      if (clazz.classFile != null && clazz.classFile.getSource() != null) {
        source = clazz.classFile.getSource();
      }
      Logger.clazz("[Loaded ".concat(name).concat(" from ").concat(source) + "]");
    }
    STRING_K_CLASS_MAP.putIfAbsent(name, clazz);
  }

  public static List<java.lang.Class> getClasses() {
    return new ArrayList<>(STRING_K_CLASS_MAP.values());
  }

  // for test
  public static void clear() {
    NATIVE_METHOD_MAP.clear();
    STRING_K_CLASS_MAP.clear();
  }
}

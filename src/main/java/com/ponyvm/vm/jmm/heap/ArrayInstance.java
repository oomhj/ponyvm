package com.ponyvm.vm.jmm.heap;

public class ArrayInstance extends Instance {

  public final int len;

  public ArrayInstance(java.lang.Class clazz, int len) {
    super(clazz);
    this.len = len;
  }
}

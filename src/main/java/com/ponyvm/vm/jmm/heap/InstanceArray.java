package com.ponyvm.vm.jmm.heap;

public class InstanceArray extends ArrayInstance {

  public final Object[] items;

  public InstanceArray(java.lang.Class clazz, Object[] items) {
    super(clazz, items.length);
    this.items = items;
  }

  @Override
  public String toString() {
    return "KArray{items=" + items.length + "}@" + this.hashCode();
  }
}

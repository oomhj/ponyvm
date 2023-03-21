package com.ponyvm.vm.classfile;

/**
 * 将jvm的属性表进行封装
 */
public class Attributes {

  public final Attribute[] attributes;

  public Attributes(int size) {
    this.attributes = new Attribute[size];
  }
}

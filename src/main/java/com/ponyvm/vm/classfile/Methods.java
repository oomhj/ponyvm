package com.ponyvm.vm.classfile;

public class Methods {

  public final MethodInfo[] methodInfos;
  public Methods(int methodCount) {
    this.methodInfos = new MethodInfo[methodCount];
  }
}

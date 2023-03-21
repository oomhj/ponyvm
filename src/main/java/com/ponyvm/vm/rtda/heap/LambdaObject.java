package com.ponyvm.vm.rtda.heap;

import java.util.List;

public class LambdaObject extends Instance {
  public final List<Object> args;

  public LambdaObject(java.lang.Class clazz, List<Object> vars) {
    super(clazz);
    this.args = vars;
  }
}

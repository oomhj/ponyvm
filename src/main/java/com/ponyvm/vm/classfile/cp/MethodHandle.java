package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class MethodHandle extends ConstantInfo {

  public final int referenceKind;
  public final int referenceIndex;

  public MethodHandle(int infoEnum, int referenceKind, int referenceIndex) {
    super(infoEnum);
    this.referenceKind = referenceKind;
    this.referenceIndex = referenceIndex;
  }
}

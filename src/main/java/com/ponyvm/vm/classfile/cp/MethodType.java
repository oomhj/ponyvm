package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class MethodType extends ConstantInfo {

  public final int descriptorIndex;

  public MethodType(int infoEnum, int descriptorIndex) {
    super(infoEnum);
    this.descriptorIndex = descriptorIndex;
  }
}

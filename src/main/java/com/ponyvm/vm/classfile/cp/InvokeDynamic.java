package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class InvokeDynamic extends ConstantInfo {

  public final int bootstrapMethodAttrIndex;
  public final int nameAndTypeIndex;

  public InvokeDynamic(int infoEnum, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
    super(infoEnum);
    this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
    this.nameAndTypeIndex = nameAndTypeIndex;
  }
}

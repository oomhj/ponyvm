package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class MethodDef extends ConstantInfo {

  public final int classIndex;
  public final int nameAndTypeIndex;

  public MethodDef(int infoEnum, int classIndex, int nameAndTypeIndex) {
    super(infoEnum);
    this.classIndex = classIndex;
    this.nameAndTypeIndex = nameAndTypeIndex;
  }
}

package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class ClassCp extends ConstantInfo {

  public final int nameIndex;

  public ClassCp(int infoEnum, int nameIndex) {
    super(infoEnum);
    this.nameIndex = nameIndex;
  }
}

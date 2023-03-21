package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class NameAndType extends ConstantInfo {

  public final int nameIndex;
  public final int descriptionIndex;

  public NameAndType(int infoEnum, int nameIndex, int descriptionIndex) {
    super(infoEnum);
    this.nameIndex = nameIndex;
    this.descriptionIndex = descriptionIndex;
  }
}

package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class StringCp extends ConstantInfo {

  public final int stringIndex;

  public StringCp(int infoEnum, int stringIndex) {
    super(infoEnum);
    this.stringIndex = stringIndex;
  }
}

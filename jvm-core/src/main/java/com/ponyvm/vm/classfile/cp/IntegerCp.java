package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class IntegerCp extends ConstantInfo {

  public final int val;

  public IntegerCp(int infoEnum, int val) {
    super(infoEnum);
    this.val = val;
  }
}

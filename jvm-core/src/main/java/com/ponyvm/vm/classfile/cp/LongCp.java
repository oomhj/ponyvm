package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class LongCp extends ConstantInfo {

  public final long val;

  public LongCp(int infoEnum, long val) {
    super(infoEnum);
    this.val = val;
  }
}
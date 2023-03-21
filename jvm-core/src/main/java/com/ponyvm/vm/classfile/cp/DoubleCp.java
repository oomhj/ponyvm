package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class DoubleCp extends ConstantInfo {

  public final double val;

  public DoubleCp(int infoEnum, double val) {
    super(infoEnum);
    this.val = val;
  }
}
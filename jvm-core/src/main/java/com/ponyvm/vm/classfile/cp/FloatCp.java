package com.ponyvm.vm.classfile.cp;

import com.ponyvm.vm.classfile.ConstantInfo;

public class FloatCp extends ConstantInfo {

  public final float val;

  public FloatCp(int infoEnum, float val) {
    super(infoEnum);
    this.val = val;
  }
}
package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class Ldc2wInst implements Instruction {
  public final Long val;
  public final Double val2;

  @Override
  public int offset() {
    return 3;
  }

  public Ldc2wInst(Long val,Double val2) {
    this.val = val;
    this.val2 = val2;
  }

  @Override
  public void execute(Frame frame) {
    if (val != null) {
      frame.pushLong(val);
    } else if (val2!= null) {
      frame.pushDouble(val2);
    }
  }

  @Override
  public String format() {
    if (val != null) {
      return "ldc2 " + val;
    }

    return "ldc2 " + val2;
  }

}
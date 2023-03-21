package com.ponyvm.vm.instruction.conversions;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class I2lInst implements Instruction {

  @Override
  public int offset() {
    return 1;
  }

  @Override
  public void execute(Frame frame) {
    Integer tmp = frame.popInt();
    frame.pushLong(tmp.longValue());
  }
}
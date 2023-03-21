package com.ponyvm.vm.instruction.conversions;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class L2fInst implements Instruction {

  @Override
  public int offset() {
    return 1;
  }

  @Override
  public void execute(Frame frame) {
    long tmp = frame.popLong();
    frame.pushFloat((float) tmp);
  }
}
package com.ponyvm.vm.instruction.conversions;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class D2iInst implements Instruction {

  @Override
  public int offset() {
    return 1;
  }

  @Override
  public void execute(Frame frame) {
    double tmp = frame.popDouble();
    frame.pushInt(((int) tmp));
  }
}
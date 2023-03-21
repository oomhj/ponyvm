package com.ponyvm.vm.instruction.extended;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class GotoWInst implements Instruction {

  public final int offset;

  public GotoWInst(int offset) {
    this.offset = offset;
  }

  @Override
  public int offset() {
    return 5;
  }

  @Override
  public void execute(Frame frame) {
    frame.nextPc = frame.getPc() + offset;
  }
}

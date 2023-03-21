package com.ponyvm.vm.instruction.extended;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class WideInst implements Instruction {

  public final int offset;
  public final Instruction inst;

  public WideInst(int offset, Instruction inst) {
    this.offset = offset;
    this.inst = inst;
  }

  @Override
  public int offset() {
    return offset;
  }

  @Override
  public void execute(Frame frame) {
    inst.execute(frame);
  }
}

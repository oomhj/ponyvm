package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class FLoadInst implements Instruction {
  public final int index;

  public FLoadInst(int index) {
    this.index = index;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    float tmp = frame.getFloat(index);
    frame.pushFloat(tmp);
  }
}
package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LSubInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    long v2 = frame.popLong();
    long v1 = frame.popLong();
    frame.pushLong(v1 - v2);
  }
}

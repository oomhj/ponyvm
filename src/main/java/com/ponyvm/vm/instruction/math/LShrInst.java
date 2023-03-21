package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LShrInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    final int v2 = frame.popInt();
    final long v1 = frame.popLong();
    frame.pushLong(v1 >> v2);
  }
}
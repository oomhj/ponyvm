package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class LOrInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long v2 = frame.popLong();
    Long v1 = frame.popLong();
    frame.pushLong(v1 | v2);
  }
}
package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class FSubInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float v2 = frame.popFloat();
    float v1 = frame.popFloat();
    frame.pushFloat(v1 - v2);
  }
}

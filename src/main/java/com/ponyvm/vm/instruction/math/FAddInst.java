package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class FAddInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float a1 = frame.popFloat();
    float a2 = frame.popFloat();
    frame.pushFloat(a1 + a2);
  }
}

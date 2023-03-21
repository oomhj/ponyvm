package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class IAddInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Integer a1 = frame.popInt();
    Integer a2 = frame.popInt();
    frame.pushInt(a1 + a2);
  }

  @Override
  public String format() {
    return "iadd";
  }
}

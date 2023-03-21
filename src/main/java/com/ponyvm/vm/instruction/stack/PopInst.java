package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class PopInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.popRef();
  }

  @Override
  public String format() {
    return "pop";
  }
}

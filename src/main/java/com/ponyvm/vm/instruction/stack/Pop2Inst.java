package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class Pop2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.popRef();
    frame.popRef();
  }
}

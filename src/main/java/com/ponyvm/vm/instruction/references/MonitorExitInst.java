package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class MonitorExitInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    // TODO ...
    frame.popRef();
//    throw new UnsupportedOperationException();
  }
}

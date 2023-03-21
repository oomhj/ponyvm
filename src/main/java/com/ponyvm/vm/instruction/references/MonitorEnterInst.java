package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class MonitorEnterInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    // TODO ...
    frame.popRef();
//    throw new UnsupportedOperationException();
  }
}

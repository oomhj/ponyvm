package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class Lconst0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushLong(0L);
  }
}

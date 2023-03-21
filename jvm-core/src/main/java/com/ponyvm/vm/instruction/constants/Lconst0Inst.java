package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class Lconst0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushLong(0L);
  }
}

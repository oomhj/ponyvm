package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class FConst0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushFloat(0.0f);
  }
}

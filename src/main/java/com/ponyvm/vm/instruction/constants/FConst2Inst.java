package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class FConst2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushFloat(2.0f);
  }
}

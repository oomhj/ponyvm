package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class IConst4Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushInt(4);
  }

  @Override
  public String format() {
    return "iconst_4";
  }
}

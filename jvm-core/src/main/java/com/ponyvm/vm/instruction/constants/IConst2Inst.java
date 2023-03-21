package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class IConst2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushInt(2);
  }

  @Override
  public String format() {
    return "iconst_2";
  }
}

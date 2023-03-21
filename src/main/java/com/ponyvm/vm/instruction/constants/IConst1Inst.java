package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

/**
 * @see IConst0Inst
 */
public class IConst1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushInt(1);
  }

  @Override
  public String format() {
    return "iconst_1";
  }
}

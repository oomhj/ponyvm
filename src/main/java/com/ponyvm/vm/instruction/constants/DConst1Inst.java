package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class DConst1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushDouble(1.0d);
  }
}

package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class DConst0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushDouble(0.0d);
  }
}

package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class DLoad2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    double tmp = frame.getDouble(2);
    frame.pushDouble(tmp);
  }
}

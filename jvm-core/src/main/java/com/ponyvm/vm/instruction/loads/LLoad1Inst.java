package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class LLoad1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.getLong(1);
    frame.pushLong(tmp);
  }
}

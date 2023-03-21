package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LLoad0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.getLong(0);
    frame.pushLong(tmp);
  }
}

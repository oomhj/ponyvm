package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LLoad2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.getLong(2);
    frame.pushLong(tmp);
  }

  @Override
  public String format() {
    return "lload2";
  }
}
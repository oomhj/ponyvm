package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class LLoad3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.getLong(3);
    frame.pushLong(tmp);
  }
}
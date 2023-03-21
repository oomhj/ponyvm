package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class FLoad0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float tmp = frame.getFloat(0);
    frame.pushFloat(tmp);
  }
}
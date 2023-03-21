package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class FLoad1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float tmp = frame.getFloat(1);
    frame.pushFloat(tmp);
  }
}
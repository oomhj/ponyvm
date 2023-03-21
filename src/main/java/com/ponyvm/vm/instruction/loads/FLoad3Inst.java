package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class FLoad3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float tmp = frame.getFloat(3);
    frame.pushFloat(tmp);
  }
}
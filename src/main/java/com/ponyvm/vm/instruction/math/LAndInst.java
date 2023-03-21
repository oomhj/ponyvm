package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class LAndInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    long a1 = frame.popLong();
    long a2 = frame.popLong();
    frame.pushLong(a2 & a1);
  }
}
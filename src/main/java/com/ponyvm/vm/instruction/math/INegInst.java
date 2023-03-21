package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class INegInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int tmp = frame.popInt();
    frame.pushInt(-tmp);
  }
}
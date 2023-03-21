package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class IXOrInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 ^ v2);
  }
}
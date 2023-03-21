package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class DRemInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    double v2 = frame.popDouble();
    double v1 = frame.popDouble();
    frame.pushDouble(v1 % v2);
  }
}
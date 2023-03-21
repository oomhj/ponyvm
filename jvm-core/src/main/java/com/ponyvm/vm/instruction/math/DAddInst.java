package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class DAddInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    double a1 = frame.popDouble();
    double a2 = frame.popDouble();
    frame.pushDouble(a1 + a2);
  }
}

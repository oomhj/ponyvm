package com.ponyvm.vm.instruction.comparisons;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class DCmpLInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Double v2 = frame.popDouble();
    Double v1 = frame.popDouble();
    if (v1.equals(v2)) {
      frame.pushInt(0);
      return;
    }
    if (v1 < v2) {
      frame.pushInt(-1);
      return;
    }
    frame.pushInt(1);
  }
}
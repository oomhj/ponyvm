package com.ponyvm.vm.instruction.comparisons;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class LCmpInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long v2 = frame.popLong();
    Long v1 = frame.popLong();
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

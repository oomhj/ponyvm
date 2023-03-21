package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class DLoadInst implements Instruction {
  public final int index;

  public DLoadInst(int index) {
    this.index = index;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    double tmp = frame.getDouble(index);
    frame.pushDouble(tmp);
  }
}
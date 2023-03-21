package com.ponyvm.vm.instruction.comparisons;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class IfICmpNeInst implements Instruction {
  public final int offset;

  public IfICmpNeInst(int offset) {
    this.offset = offset;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void execute(Frame frame) {
    Integer val2= frame.popInt();
    Integer val1= frame.popInt();
    if (!val1.equals(val2)) {
      frame.nextPc = frame.getPc() + offset;
    }
  }

  @Override
  public String format() {
    return "if_cmpne " + offset;
  }
}
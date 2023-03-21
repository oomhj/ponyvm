package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class LLoadInst implements Instruction {
  public final int index;

  public LLoadInst(int index) {
    this.index = index;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.getLong(index);
    frame.pushLong(tmp);
  }
}

package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Instance;

public class ALoadInst implements Instruction {

  public final int index;

  public ALoadInst(int index) {
    this.index = index;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    Instance tmp = frame.getRef(index);
    frame.pushRef(tmp);
  }

  @Override
  public String format() {
    return "aload " + index;
  }
}
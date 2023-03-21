package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Instance;

public class ALoad0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Instance tmp = frame.getRef(0);
    frame.pushRef(tmp);
  }

  @Override
  public String format() {
    return "aload_0";
  }
}

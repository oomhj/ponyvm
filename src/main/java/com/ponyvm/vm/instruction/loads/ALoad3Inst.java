package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Instance;

public class ALoad3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Instance tmp = frame.getRef(3);
    frame.pushRef(tmp);
  }

  @Override
  public String format() {
    return "aload_3";
  }
}

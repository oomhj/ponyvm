package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Instance;

public class ALoad2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Instance tmp = frame.getRef(2);
    frame.pushRef(tmp);
  }

  @Override
  public String format() {
    return "aload_2";
  }
}

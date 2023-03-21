package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Instance;

public class SwapInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Object v2 = frame.popRef();
    Object v1 = frame.popRef();
    frame.pushRef((Instance) v2);
    frame.pushRef((Instance) v1);
  }
}

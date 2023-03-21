package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Instance;
import com.ponyvm.vm.jmm.heap.InstanceArray;

public class AALoadInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int index = frame.popInt();
    InstanceArray array = (InstanceArray) frame.popRef();
    Object item = array.items[index];
    frame.pushRef((Instance) item);
  }

  @Override
  public String format() {
    return "aaload";
  }
}

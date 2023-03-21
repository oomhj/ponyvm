package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Instance;

public class AStore1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Object tmp = frame.popRef();
    frame.setRef(1, (Instance) tmp);
  }

  @Override
  public String format() {
    return "astore_1";
  }
}
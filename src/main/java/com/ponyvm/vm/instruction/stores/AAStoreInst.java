package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.InstanceArray;

public class AAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Object val = frame.popRef();
    Integer index = frame.popInt();
    InstanceArray array = (InstanceArray) frame.popRef();
    array.items[index] = val;
  }

  @Override
  public String format() {
    return "aastore";
  }
}
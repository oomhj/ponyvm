package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.PrimitiveArray;

public class IAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int val = frame.popInt();
    int index = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[index] = val;
  }

  @Override
  public String format() {
    return "iastore";
  }
}
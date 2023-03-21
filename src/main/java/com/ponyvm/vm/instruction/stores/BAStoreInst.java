package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.PrimitiveArray;

public class BAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int val = frame.popInt();
    int index = frame.popInt();
    PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[index] = val;
  }
}
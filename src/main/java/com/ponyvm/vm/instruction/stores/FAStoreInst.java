package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.PrimitiveArray;

public class FAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float val = frame.popFloat();
    int index = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.floats[index] = val;
  }
}
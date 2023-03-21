package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;

public class FAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float val = frame.popFloat();
    int index = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.floats[index] = val;
  }
}
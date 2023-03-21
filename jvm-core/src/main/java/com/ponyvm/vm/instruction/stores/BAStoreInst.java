package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;

public class BAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int val = frame.popInt();
    int index = frame.popInt();
    PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[index] = val;
  }
}
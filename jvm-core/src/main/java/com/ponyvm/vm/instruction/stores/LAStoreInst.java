package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;

public class LAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    long val = frame.popLong();
    int index = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.longs[index] = val;
  }

  @Override
  public String format() {
    return "lastore";
  }
}
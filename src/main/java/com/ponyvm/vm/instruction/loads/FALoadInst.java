package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.PrimitiveArray;

public class FALoadInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int index = frame.popInt();
    PrimitiveArray array = (PrimitiveArray) frame.popRef();
    frame.pushFloat(array.floats[index]);
  }
}
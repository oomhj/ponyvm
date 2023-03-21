package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.PrimitiveArray;

public class DAStoreInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    double val = frame.popDouble();
    int index = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.doubles[index] = val;
  }
}
package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.ArrayInstance;

public class ArrayLengthInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    ArrayInstance arr = (ArrayInstance) frame.popRef();
    frame.pushInt(arr.len);
  }

  @Override
  public String format() {
    return "arraylength";
  }
}

package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.PrimitiveArray;

public class CAloadInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int index = frame.popInt();
    PrimitiveArray array = (PrimitiveArray) frame.popRef();
    frame.pushInt(array.ints[index]);
  }

  @Override
  public String format() {
    return "caload";
  }
}
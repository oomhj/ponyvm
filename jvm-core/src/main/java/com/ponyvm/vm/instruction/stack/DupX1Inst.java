package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.Slot;

public class DupX1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Slot s1 = frame.popSlot();
    Slot s2 = frame.popSlot();
    frame.pushSlot(s1);
    frame.pushSlot(s2);
    frame.pushSlot(s1);
  }

  @Override
  public String format() {
    return "dupx1";
  }
}
package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.Slot;

public class DupInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    final Slot val = frame.pop();
    frame.push(val);
    frame.push(val);
  }

  @Override
  public String format() {
    return "dup";
  }
}

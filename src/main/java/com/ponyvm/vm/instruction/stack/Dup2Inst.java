package com.ponyvm.vm.instruction.stack;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.Slot;

public class Dup2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    final Slot v2 = frame.pop();
    final Slot v1 = frame.pop();
    frame.push(v1);
    frame.push(v2);
    frame.push(v1);
    frame.push(v2);
  }
}

package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class ILoad0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int tmp = frame.getInt(0);
    frame.pushInt(tmp);
  }

  @Override
  public String format() {
    return "iload_0";
  }
}

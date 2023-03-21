package com.ponyvm.vm.instruction.loads;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class ILoad1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Integer tmp = frame.getInt(1);
    frame.pushInt(tmp);
  }

  @Override
  public String format() {
    return "iload_1";
  }
}

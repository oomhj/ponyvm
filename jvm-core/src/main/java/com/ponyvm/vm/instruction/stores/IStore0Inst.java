package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class IStore0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Integer tmp = frame.popInt();
    frame.setInt(0, tmp);
  }

  @Override
  public String format() {
    return "istore_0";
  }
}

package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class LStore3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.popLong();
    frame.setLong(3, tmp);
  }
}
package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LStore1Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.popLong();
    frame.setLong(1, tmp);
  }
}

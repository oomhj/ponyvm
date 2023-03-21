package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class FStore3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    float tmp = frame.popFloat();
    frame.setFloat(3, tmp);
  }
}

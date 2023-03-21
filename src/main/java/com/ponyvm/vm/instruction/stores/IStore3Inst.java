package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class IStore3Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int tmp = frame.popInt();
    frame.setInt(3, tmp);
  }

  @Override
  public String format() {
    return "istore_3";
  }
}

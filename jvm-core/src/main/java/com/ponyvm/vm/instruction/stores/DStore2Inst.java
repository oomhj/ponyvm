package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class DStore2Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    double tmp = frame.popDouble();
    frame.setDouble(2, tmp);
  }
}

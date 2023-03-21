package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class DStoreNInst implements Instruction {
  public final int n;

  public DStoreNInst(int n) {
    this.n = n;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    double tmp = frame.popDouble();
    frame.setDouble(n, tmp);
  }
}

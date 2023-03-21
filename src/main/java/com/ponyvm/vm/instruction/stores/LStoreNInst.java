package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

public class LStoreNInst implements Instruction {
  public final int n;

  public LStoreNInst(int n) {
    this.n = n;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    Long tmp = frame.popLong();
    frame.setLong(n, tmp);
  }
}

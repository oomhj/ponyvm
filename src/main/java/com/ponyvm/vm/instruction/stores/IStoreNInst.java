package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class IStoreNInst implements Instruction {
  public final int n;

  public IStoreNInst(int n) {
    this.n = n;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    Integer tmp = frame.popInt();
    frame.setInt(n, tmp);
  }

  @Override
  public String format() {
    return "istore " + n;
  }

  @Override
  public String toString() {
    return "IStoreNInst{" +
        "n=" + n +
        '}';
  }
}

package com.ponyvm.vm.instruction.extended;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

public class MultiANewArrayInst implements Instruction{

  public final int index;
  public final int dimensions;

  public MultiANewArrayInst(int index, int dimensions) {
    this.index = index;
    this.dimensions = dimensions;
  }

  @Override
  public int offset() {
    return 4;
  }

  @Override
  public void execute(Frame frame) {
    throw new UnsupportedOperationException(MultiANewArrayInst.class.getName());
  }
}
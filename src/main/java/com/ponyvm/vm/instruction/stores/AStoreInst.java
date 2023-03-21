package com.ponyvm.vm.instruction.stores;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Instance;

public class AStoreInst implements Instruction {

  public final int index;

  public AStoreInst(int index) {
    this.index = index;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    Object tmp = frame.popRef();
    frame.setRef(index, (Instance) tmp);
  }

  @Override
  public String format() {
    return "astore " + index;
  }

}
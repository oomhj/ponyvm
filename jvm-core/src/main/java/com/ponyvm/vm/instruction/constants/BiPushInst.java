package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class BiPushInst implements Instruction {

  public final byte val;

  public BiPushInst(byte val) {
    this.val = val;
  }

  @Override
  public int offset() {
    return 2;
  }

  @Override
  public void execute(Frame frame) {
    frame.pushInt((int) (this.val));
  }

  @Override
  public String format() {
    return "bipush " + val;
  }
}

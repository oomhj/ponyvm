package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;

public class SiPushInst implements Instruction {

  public final short val;

  public SiPushInst(short val) {
    this.val = val;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void execute(Frame frame) {
    frame.pushInt((int) (this.val));
  }

  @Override
  public String format() {
    return "sipush " + val;
  }
}

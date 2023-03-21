package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class LShlInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int v2 = frame.popInt();
    long v1 = frame.popLong();
    int s = v2 & 0x1f;
    long ret = v1 << s;
    frame.pushLong(ret);
  }
}
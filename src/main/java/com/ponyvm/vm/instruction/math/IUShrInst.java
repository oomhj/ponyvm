package com.ponyvm.vm.instruction.math;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

public class IUShrInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    int s = v2 & 0x1f;

    if (v1 >= 0) {
      int ret = v1 >> s;
      frame.pushInt(ret);
      return;
    }
    int ret = (v1 >> s) + (2 << ~s);
    frame.pushInt(ret);
  }

  @Override
  public String format() {
    return "iushr";
  }
}
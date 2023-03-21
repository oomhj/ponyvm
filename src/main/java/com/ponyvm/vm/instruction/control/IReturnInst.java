package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.util.Utils;

public class IReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
//    Integer tmp = frame.popInt();
//    frame.thread.popFrame();
//    if (!frame.thread.empty()) {
//      frame.thread.currentFrame().pushInt(tmp);
//    }
//    System.out.println("do ret " + tmp);
    Utils.doReturn1();
  }

  @Override
  public String format() {
    return "ireturn";
  }
}

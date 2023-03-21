package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.util.Utils;

public class LReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
//    Long tmp = frame.popLong();
//    frame.thread.popFrame();
//    if (!frame.thread.empty()) {
//      frame.thread.currentFrame().pushLong(tmp);
//    }
//    System.out.println("do ret " + tmp);

    Utils.doReturn2();
  }
}
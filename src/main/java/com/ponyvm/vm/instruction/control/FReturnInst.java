package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.util.Utils;

public class FReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
//    float tmp = frame.popFloat();
//    frame.thread.popFrame();
//    if (!frame.thread.empty()) {
//      frame.thread.currentFrame().pushFloat(tmp);
//    }
//    System.out.println("do ret " + tmp);
    Utils.doReturn1();
  }
}
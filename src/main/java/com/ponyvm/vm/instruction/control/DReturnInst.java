package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.util.Utils;

public class DReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
//    double tmp = frame.popDouble();
//    frame.thread.popFrame();
//    if (!frame.thread.empty()) {
//      frame.thread.currentFrame().pushDouble(tmp);
//    }
//    System.out.println("do ret " + tmp);
    Utils.doReturn2();
  }
}
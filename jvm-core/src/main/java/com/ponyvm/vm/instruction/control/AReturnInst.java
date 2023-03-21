package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.util.Utils;

public class AReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
//    Object tmp = frame.popRef();
//    frame.thread.popFrame();
//    if (!frame.thread.empty()) {
//      frame.thread.currentFrame().pushRef(tmp);
//    }
//    System.out.println("do ret " + tmp);
    Utils.doReturn1();
  }

  @Override
  public String format() {
    return "areturn";
  }
}

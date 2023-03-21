package com.ponyvm.vm.instruction.control;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.util.Utils;

public class ReturnInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    // do nothing
//    frame.thread.popFrame();
    Utils.doReturn0();
  }

  @Override
  public String format() {
    return "return";
  }
}

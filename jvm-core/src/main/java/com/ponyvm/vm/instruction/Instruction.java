package com.ponyvm.vm.instruction;

import com.ponyvm.vm.rtda.Frame;

public interface Instruction {

  default int offset() {
    return 1;
  }

  void execute(Frame frame);

  default String debug(String prefix){
    return prefix.concat(this.format());
  }

  default String format() {
    return this.getClass().getSimpleName();
  }
}

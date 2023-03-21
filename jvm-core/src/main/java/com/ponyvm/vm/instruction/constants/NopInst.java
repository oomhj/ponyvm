package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.rtda.Frame;

/**
 * <p><a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.nop">nop</a></p>
 * <p>Do nothing.</p>
 * <p>Operand Stack</p>
 * <pre>
 * No change
 * </pre>
 */
public class NopInst implements Instruction {

  @Override
  public void execute(Frame frame) {
  }
}

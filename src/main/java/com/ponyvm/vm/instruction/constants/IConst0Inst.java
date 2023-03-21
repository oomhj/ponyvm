package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;

/**
 * <p><a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_i">iconst_i</a></p>
 * <p>Push the int constant <i> (-1, 0, 1, 2, 3, 4 or 5) onto the operand stack.</p>
 * <p>Operand Stack</p>
 * <pre>
 * ... →
 * ..., <i>
 * </pre>
 */
public class IConst0Inst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushInt(0);
  }

  @Override
  public String format() {
    return "iconst_0";
  }
}

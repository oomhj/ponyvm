package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;

//

/**
 * <p><a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aconst_null">const_null</a>
 * <p>Push the null object reference onto the operand stack.
 * <p>operand stack
 * <pre>
 * ... →
 * ..., null
 * </pre>
 */
public class AconstNullInst implements Instruction {

  @Override
  public void execute(Frame frame) {
    frame.pushRef(null);
  }

  @Override
  public String format() {
    return "aconst_null";
  }
}

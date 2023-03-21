package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.Slot;
import com.ponyvm.vm.jmm.UnionSlot;
import com.ponyvm.vm.jmm.heap.Field;
import com.ponyvm.vm.jmm.heap.Instance;

public class PutFieldInst implements Instruction {
  public final String clazz;
  public final String fieldName;
  public final String fieldDescriptor;

  @Override
  public int offset() {
    return 3;
  }

  public PutFieldInst(String clazz, String fieldName, String fieldDescriptor) {
    this.clazz = clazz;
    this.fieldName = fieldName;
    this.fieldDescriptor = fieldDescriptor;
  }

  @Override
  public void execute(Frame frame) {
    UnionSlot us = null;
    if (fieldDescriptor.equals("J") || fieldDescriptor.equals("D")) {
      final Slot low = frame.pop();
      final Slot high = frame.pop();
      us = UnionSlot.of(high, low);
    } else {
      us = UnionSlot.of(frame.pop());
    }

    final Instance self = frame.popRef();
    Field field = self.getField(fieldName, fieldDescriptor);
    field.set(us);
  }

  @Override
  public String format() {
    return "putfield " + clazz + " " + fieldName + " " + fieldDescriptor;
  }
}

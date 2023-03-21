package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.UnionSlot;
import com.ponyvm.vm.rtda.heap.Field;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.util.Utils;

public class GetFieldInst implements Instruction {

  public final String clazz;
  public final String fieldName;
  public final String fieldDescriptor;

  @Override
  public int offset() {
    return 3;
  }

  public GetFieldInst(String clazz, String fieldName, String fieldDescriptor) {
    this.clazz = clazz;
    this.fieldName = fieldName;
    this.fieldDescriptor = fieldDescriptor;
  }


  @Override
  public void execute(Frame frame) {
    // hack for java/nio/charset/Charset name Ljava/lang/String;
    if (clazz.equals("java/nio/charset/Charset") && fieldName.equals("name")) {
      Instance obj = ((Instance) frame.popRef());
      Field field = obj.getField(fieldName, fieldDescriptor);
      field.val = UnionSlot.of(Utils.str2Obj("UTF-8", obj.clazz.classLoader));
      field.get(frame);
      return;
    }

    Instance obj = ((Instance) frame.popRef());
    Field field = obj.getField(fieldName, fieldDescriptor);
    field.get(frame);
  }

  @Override
  public String format() {
    return "getfield " + clazz + " " + fieldName + " " + fieldDescriptor;
  }

  @Override
  public String toString() {
    return "GetFieldInst{" +
        "clazz='" + clazz + '\'' +
        ", fieldName='" + fieldName + '\'' +
        ", fieldDescriptor='" + fieldDescriptor + '\'' +
        '}';
  }
}

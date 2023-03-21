package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;


import com.ponyvm.vm.interpret.Interpreter;
import com.ponyvm.vm.rtda.Frame;
import com.ponyvm.vm.rtda.heap.Heap;
import com.ponyvm.vm.rtda.heap.Class;
import com.ponyvm.vm.rtda.heap.Field;
import com.ponyvm.vm.rtda.heap.Method;

import java.util.ArrayList;
import java.util.List;

public class GetStaticInst implements Instruction {

  public final String clazz;
  public final String fieldName;
  public final String fieldDescriptor;

  @Override
  public int offset() {
    return 3;
  }

  public GetStaticInst(String clazz, String fieldName, String fieldDescriptor) {
    this.clazz = clazz;
    this.fieldName = fieldName;
    this.fieldDescriptor = fieldDescriptor;
  }


  @Override
  public void execute(Frame frame) {
    Class cls = Heap.findClass(clazz);
    if (cls == null) {
      cls = frame.method.clazz.classLoader.loadClass(clazz);
    }

    if (!cls.getStat()) {
      Method cinit = cls.getMethod("<clinit>", "()V");
      if (cinit == null) {
        throw new IllegalStateException();
      }

      cls.setStat(1);
      Interpreter.execute(cinit);
      cls.setStat(2);
    }

    Field field = cls.getField(fieldName, fieldDescriptor);
    if (field == null) {
      // interface
      if (cls.interfaceNames.isEmpty()) {
        throw new IllegalStateException();
      }

      // already load interface
      if (cls.getInterfaces().isEmpty()) {
        List<Class> interfaces = new ArrayList<>();
        for (String interfaceName : cls.interfaceNames) {
          Class tmp = Heap.findClass(interfaceName);
          if (tmp == null) {
            tmp = frame.method.clazz.classLoader.loadClass(interfaceName);
          }

          interfaces.add(tmp);

          if (!tmp.getStat()) {
            Method cinit = tmp.getClinitMethod();
            if (cinit == null) {
              throw new IllegalStateException();
            }

            tmp.setStat(1);
            Interpreter.execute(cinit);
            tmp.setStat(2);
          }
        }
        cls.setInterfaces(interfaces);
      }

      if (!cls.getInterfaces().isEmpty()) {
        for (Class intClass : cls.getInterfaces()) {
          field = intClass.getField(fieldName, fieldDescriptor);
          if (field != null) {
            break;
          }
        }
      }
    }

    if (field == null) {
      throw new IllegalStateException();
    }

    if (field.val == null) {
      throw new IllegalStateException();
    }

    field.get(frame);
  }


  @Override
  public String format() {
    return "getstatic " + clazz + " " + fieldName + " " + fieldDescriptor;
  }

  @Override
  public String toString() {
    return "GetStaticInst{" +
        "clazz='" + clazz + '\'' +
        ", fieldName='" + fieldName + '\'' +
        ", fieldDescriptor='" + fieldDescriptor + '\'' +
        '}';
  }
}

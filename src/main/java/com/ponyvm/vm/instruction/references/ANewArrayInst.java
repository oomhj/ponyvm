package com.ponyvm.vm.instruction.references;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Instance;
import com.ponyvm.vm.jmm.heap.InstanceArray;
import com.ponyvm.vm.util.Utils;

public class ANewArrayInst implements Instruction {

  public final String className;

  public ANewArrayInst(String className) {
    this.className = className;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void execute(Frame frame) {
    Class aClass = frame.method.clazz.classLoader.loadClass(className);
    Utils.clinit(aClass);

    int count = frame.popInt();
    String name = "[L" + aClass.name + ";";

    Class clazz = Heap.findClass(name);
    if (clazz == null) {
      clazz = new Class(1, name, aClass.classLoader, null);
      clazz.setSuperClass(Heap.findClass("java/lang/Object"));
      clazz.setStat(2);
      Heap.registerClass(name, clazz);
    }
    Instance[] objs = new Instance[count];
    InstanceArray instanceArray = new InstanceArray(clazz, objs);
    frame.pushRef(instanceArray);
  }

  @Override
  public String format() {
    return "anewarray";
  }
}
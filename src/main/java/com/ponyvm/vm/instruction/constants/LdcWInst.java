package com.ponyvm.vm.instruction.constants;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.interpret.Interpreter;
import com.ponyvm.vm.jmm.Frame;
import com.ponyvm.vm.jmm.UnionSlot;
import com.ponyvm.vm.jmm.heap.Class;
import com.ponyvm.vm.jmm.heap.Field;
import com.ponyvm.vm.jmm.heap.Heap;
import com.ponyvm.vm.jmm.heap.Instance;
import com.ponyvm.vm.jmm.heap.InstanceArray;

public class LdcWInst implements Instruction {

  public final String descriptor;
  public final Object val;

  @Override
  public int offset() {
    return 3;
  }

  public LdcWInst(String descriptor, Object val) {
    this.descriptor = descriptor;
    this.val = val;
  }

  @Override
  public void execute(Frame frame) {
    switch (descriptor) {
      case "I":
        frame.pushInt(((Integer) val));
        break;
      case "F":
        frame.pushFloat(((float) val));
        break;
      case "Ljava/lang/String":
        Class klass = Heap.findClass("java/lang/String");
        if (klass == null) {
          klass = frame.method.clazz.classLoader.loadClass("java/lang/String");
        }
        if (!klass.getStat()) {
          klass.setStat(1);
          Interpreter.execute(klass.getMethod("<clinit>", "()V"));
          klass.setStat(2);
        }
        Instance object = klass.newInstance();
        Field field = object.getField("value", "[C");
        Class arrClazz = new Class(1, "[C", frame.method.clazz.classLoader, null);

        char[] chars = val.toString().toCharArray();
        Character[] characters = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
          characters[i] = chars[i];
        }
        InstanceArray arr = new InstanceArray(arrClazz, characters);
        field.val = UnionSlot.of(arr);
        frame.pushRef(object);
        break;
      default:
        frame.pushRef((Instance) val);
        break;
    }
  }

  @Override
  public String format() {
    return "ldcw " + descriptor + " " + val;
  }
}

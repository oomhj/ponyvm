package com.ponyvm.vm.jmm.heap;

import com.ponyvm.vm.jmm.UnionSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Instance implements Cloneable {

  public final List<Field> fields;
  public final java.lang.Class clazz;
  private Instance superInstance;

  // for class obj
  private java.lang.Class metaClass;

  // extra
  private Object extra;

  public Instance(java.lang.Class clazz) {
    fields = new ArrayList<>();
    this.clazz = clazz;
  }

  public Instance(List<Field> fields, java.lang.Class clazz) {
    this.fields = fields;
    this.clazz = clazz;
  }

  public Field getField(String fieldName, String fieldDescriptor) {
    // this object
    for (Field field : fields) {
      if (Objects.equals(field.name, fieldName) && Objects.equals(field.descriptor, fieldDescriptor)) {
        return field;
      }
    }

    if (this.superInstance == null) {
      return null;
    }

    // super object
    return this.superInstance.getField(fieldName, fieldDescriptor);
  }

  public void setSuperInstance(Instance superInstance) {
    this.superInstance = superInstance;
  }

  public void setField(String name, String desc, UnionSlot val) {
    Field field = this.getField(name, desc);
    field.val = val;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public String toString() {
    return clazz.name + "@" + this.hashCode();
  }

  public java.lang.Class getMetaClass() {
    return metaClass;
  }

  public void setMetaClass(java.lang.Class metaClass) {
    this.metaClass = metaClass;
  }

  public Object getExtra() {
    return extra;
  }

  public void setExtra(Object extra) {
    this.extra = extra;
  }
}

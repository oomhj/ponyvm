package com.ponyvm.vm.rtda.heap;

public class PrimitiveArray extends ArrayInstance {

  public int[] ints;
  public long[] longs;
  public float[] floats;
  public double[] doubles;

  private PrimitiveArray(java.lang.Class clazz, int size) {
    super(clazz, size);
  }

  public PrimitiveArray cloneInstance() {
    final PrimitiveArray clone = new PrimitiveArray(this.clazz, len);
    clone.ints = ints;
    clone.longs = longs;
    clone.floats = floats;
    clone.doubles = doubles;
    return clone;
  }

  public static PrimitiveArray charArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[C");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  public static PrimitiveArray boolArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[Z");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  public static PrimitiveArray byteArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[B");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  public static PrimitiveArray shortArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[S");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  public static PrimitiveArray intArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[I");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  public static PrimitiveArray longArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[J");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.longs = new long[size];
    return array;
  }

  public static PrimitiveArray floatArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[F");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.floats = new float[size];
    return array;
  }

  public static PrimitiveArray doubleArray(int size) {
    final java.lang.Class arrCls = Heap.findClass("[D");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.doubles = new double[size];
    return array;
  }
}
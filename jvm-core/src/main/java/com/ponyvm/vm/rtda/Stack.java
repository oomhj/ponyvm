//package com.ponyvm.vm.jmm;
//
//public class Stack<E> extends java.util.Stack<E> {
//
//  public final int maxSize;
//
//  public Stack(int maxSize) {
//    this.maxSize = maxSize;
//  }
//
//  @Override
//  public E push(E item) {
//    if (this.size() >= maxSize) {
//      for (int i = 0; i < this.maxSize; i++) {
//        System.err.println(this.get(i));
//      }
//      throw new IllegalStateException("stack overflow");
//    }
//    return super.push(item);
//  }
//
//  @Override
//  public synchronized E pop() {
//    E pop = super.pop();
//    if (pop == null) {
//      throw new IllegalStateException();
//    }
//    return pop;
//  }
//}

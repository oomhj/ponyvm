package com.ponyvm.vm.rtda;

import com.ponyvm.vm.instruction.Instruction;
import com.ponyvm.vm.rtda.heap.Instance;
import com.ponyvm.vm.rtda.heap.Method;

import java.util.Map;

public class Frame {

    public final Method method;
    private final LocalVars localVars;
    private final OperandStack operandStack;
    private final Map<Integer, Instruction> instructionMap;
    public final Thread thread;
    public int nextPc;
    private int pc;

    public int stat;

    public Frame(Method method) {
        this.method = method;
        this.localVars = new LocalVars(method.maxLocals);
        this.operandStack = new OperandStack(method.maxStacks);
        this.thread = MetaSpace.getMainEnv();
        this.instructionMap = method.instructionMap;
    }

    public Frame(Method method, LocalVars localVars, Thread thread) {
        this.method = method;
        this.localVars = localVars;
        this.operandStack = new OperandStack(method.maxStacks);
        this.thread = thread;
        this.instructionMap = method.instructionMap;
    }

    public Instruction getInst() {
        this.pc = nextPc;
        return this.instructionMap.get(this.pc);
    }

    // operand stack operation

    public void pushInt(int val) {
        this.operandStack.pushInt(val);
    }

    public int popInt() {
        return this.operandStack.popInt();
    }

    public void pushLong(long val) {
        this.operandStack.pushLong(val);
    }

    public long popLong() {
        return this.operandStack.popLong();
    }

    public void pushFloat(float val) {
        this.operandStack.pushFloat(val);
    }

    public float popFloat() {
        return this.operandStack.popFloat();
    }

    public void pushDouble(double val) {
        this.operandStack.pushDouble(val);
    }

    public double popDouble() {
        return this.operandStack.popDouble();
    }

    public void pushRef(Instance val) {
        this.operandStack.pushRef(val);
    }

    public Instance popRef() {
        return this.operandStack.popRef();
    }

    public Slot popSlot() {
        return this.operandStack.popSlot();
    }

    public void pushSlot(Slot val) {
        this.operandStack.pushSlot(val);
    }

    // local vars operation

    public void setInt(int index, int val) {
        this.localVars.setInt(index, val);
    }

    public int getInt(int index) {
        return this.localVars.getInt(index);
    }

    public void setFloat(int index, Float val) {
        this.localVars.setFloat(index, val);
    }

    public Float getFloat(int index) {
        return this.localVars.getFloat(index);
    }

    public Long getLong(int index) {
        return this.localVars.getLong(index);
    }

    public void setLong(int index, Long val) {
        this.localVars.setLong(index, val);
    }

    public void setDouble(int index, Double val) {
        this.localVars.setDouble(index, val);
    }

    public Double getDouble(int index) {
        return this.localVars.getDouble(index);
    }

    public void setRef(int index, Instance ref) {
        this.localVars.setRef(index, ref);
    }

    public Instance getRef(int index) {
        return this.localVars.getRef(index);
    }

    public LocalVars getLocalVars() {
        return this.localVars;
    }

    public OperandStack getOperandStack() {
        return this.operandStack;
    }

    public int getPc() {
        return pc;
    }

    public String debugNextPc(String space) {
        return space.concat("nextPc = ").concat(Integer.toString(nextPc)).concat("\n");
    }

    public String debugLocalVars(String space) {
        StringBuilder sb = new StringBuilder();
        sb.append(localVars.debug(space));
        return sb.toString();
    }

    public String debugOperandStack(String space) {
        StringBuilder sb = new StringBuilder();
        sb.append(operandStack.debug(space));
        return sb.toString();
    }

    public String getCurrentMethodFullName() {
        return this.method.clazz.name + "." + this.method.name;
    }

    public int getCurrentLine() {
        return this.method.getLine(this.pc);
    }

    public String getCurrentSource() {
        return this.method.clazz.getSource();
    }

    public Slot pop() {
        return this.operandStack.popSlot();
    }

    public void push(Slot val) {
        this.operandStack.pushSlot(val);
    }

    public void set(int i, Slot val) {
        this.localVars.set(i, val);
    }

    public Instance getThis(int size) {
        final Slot[] slots = this.operandStack.getSlots();
        return slots[this.operandStack.getTop() - size].ref;
    }
}

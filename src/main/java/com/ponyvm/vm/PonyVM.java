package com.ponyvm.vm;

import java.util.Stack;

public class PonyVM {
    private Stack<Byte> OperandStack;
    private Bus IOBus;

    private byte PC = 0;

    public PonyVM() {
        this.OperandStack = new Stack<Byte>();
        this.IOBus = new Bus();
    }

    public void runCPU() {
        boolean end = false;
        while (!end) {
            OpCodes opCode = OpCodes.byIndex(IOBus.load(PC++));
            switch (opCode) {
                case PUSH:
                    OperandStack.push(IOBus.load(PC++));
                    break;
                case POP:
                    OperandStack.pop();
                    break;
                case END:
                    end = true;
                    break;
                case LDR:
                    OperandStack.push(IOBus.load(IOBus.load(PC++)));
                    break;
                case STR:
                    IOBus.store(IOBus.load(PC++), OperandStack.lastElement());
                    break;
                default:
                    break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadBinaryProgram(byte[] bin) {
        for (int i = 0; i < bin.length; i++) {
            IOBus.store((byte) i, bin[i]);
        }
    }
}

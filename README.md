Java实现的RISC-V架构教学虚拟机。采用RV32IMC指令集

ponyvm-rv32i 构建打包

mvn compile assembly:single

ponyvm-rv32i 运行

java -jar PonyVM-0.3.0.jar -f pi-imc.bin

命令行 参数

-f rom文件

C样例编译 采用了ESP32C3的工具链 riscv32-esp-elf-gcc

riscv32-esp-elf-gcc -march=rv32imc pi.c -o pi-imc.bin

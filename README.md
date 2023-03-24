# ponyvm
RV32分支，采用RV32I指令集

采用ESP32C3工具链
riscv32-esp-elf-gcc

C样例编译

riscv32-esp-elf-gcc -march=rv32i loop.s -o loop.bin

ponyvm-rv32i 构建打包

mvn compile assembly:single

ponyvm-rv32i 运行

java -jar PonyVM-0.0.1.jar -f pi.bin

命令行 参数

-f rom文件










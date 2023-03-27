	.file	"pi.c"
	.option nopic
	.attribute arch, "rv32i2p1_m2p0"
	.attribute unaligned_access, 0
	.attribute stack_align, 16
	.text
	.globl	__muldf3
	.globl	__floatsidf
	.globl	__divdf3
	.globl	__adddf3
	.globl	__fixdfsi
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	sw	s2,20(sp)
	sw	s3,16(sp)
	addi	s0,sp,32
	sw	zero,-32(s0)
	sw	zero,-28(s0)
	sw	zero,-20(s0)
	j	.L2
.L5:
	lw	a5,-20(s0)
	andi	a5,a5,1
	beq	a5,zero,.L3
	lui	a5,%hi(.LC0)
	lw	a4,%lo(.LC0)(a5)
	lw	a5,%lo(.LC0+4)(a5)
	j	.L4
.L3:
	lui	a5,%hi(.LC1)
	lw	a4,%lo(.LC1)(a5)
	lw	a5,%lo(.LC1+4)(a5)
.L4:
	lui	a3,%hi(.LC2)
	lw	a2,%lo(.LC2)(a3)
	lw	a3,%lo(.LC2+4)(a3)
	mv	a0,a4
	mv	a1,a5
	call	__muldf3
	mv	a4,a0
	mv	a5,a1
	mv	s2,a4
	mv	s3,a5
	lw	a5,-20(s0)
	slli	a5,a5,1
	addi	a5,a5,1
	mv	a0,a5
	call	__floatsidf
	mv	a4,a0
	mv	a5,a1
	mv	a2,a4
	mv	a3,a5
	mv	a0,s2
	mv	a1,s3
	call	__divdf3
	mv	a4,a0
	mv	a5,a1
	mv	a2,a4
	mv	a3,a5
	lw	a0,-32(s0)
	lw	a1,-28(s0)
	call	__adddf3
	mv	a4,a0
	mv	a5,a1
	sw	a4,-32(s0)
	sw	a5,-28(s0)
	lw	a5,-20(s0)
	addi	a5,a5,1
	sw	a5,-20(s0)
.L2:
	lw	a4,-20(s0)
	li	a5,8192
	addi	a5,a5,1807
	ble	a4,a5,.L5
	lui	a5,%hi(.LC3)
	lw	a2,%lo(.LC3)(a5)
	lw	a3,%lo(.LC3+4)(a5)
	lw	a0,-32(s0)
	lw	a1,-28(s0)
	call	__muldf3
	mv	a4,a0
	mv	a5,a1
	mv	a0,a4
	mv	a1,a5
	call	__fixdfsi
	mv	a5,a0
	mv	a0,a5
	lw	ra,28(sp)
	lw	s0,24(sp)
	lw	s2,20(sp)
	lw	s3,16(sp)
	addi	sp,sp,32
	jr	ra
	.size	main, .-main
	.section	.rodata
	.align	3
.LC0:
	.word	0
	.word	-1074790400
	.align	3
.LC1:
	.word	0
	.word	1072693248
	.align	3
.LC2:
	.word	0
	.word	1074790400
	.align	3
.LC3:
	.word	0
	.word	1093567616
	.ident	"GCC: (xPack GNU RISC-V Embedded GCC x86_64) 12.2.0"

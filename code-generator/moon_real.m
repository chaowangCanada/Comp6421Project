f11496724651	sw	4(R2), R3	% store return address
	lw	R13, 8(R2)	%  save input to var def
	sw	-8(R2), R13	% 
	addi	R12, R0, 1	% 
	sw	-12(R2), R12	% 
	lw	R15, -8(R2)	% 
	lw	R8, -12(R2)	% 
	add	R14, R15, R8	% 
	sw	-4(R2), R14	% 
	lw	R6, -4(R2)	% 
	sw	-16(R2), R6	% 
	lw	R5, -16(R2)	% 
	add	R3, R0, R5	% return value is a register value, get its value
	lw	R10, 4(R2)	% get return address
	lw	R2, 0(R2)	% load the previous frame pointer address
	addi	R1, R1, 16	% reset the stack pointer
	jr	R13	% 
	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 12	% set the stack pointer to the top position of the stack
	addi	R9, R0, 64	% 
	sw	-4(R2), R9	% 
	lw	R4, -4(R2)	% 
	sw	-12(R2), R4	% 
	lw	R7, -12(R2)	% 
	sw	-4(R1), R7	% add parameter
	sw	-16(R1), R2	% store the previous frame pointer
	addi	R2, R1, -16	% update the frame pointer
	addi	R1, R2, -4	% update the stack pointer
	jl	R3, f11496724651	% store return address and jump to null
	sw	-8(R2), R3	% 
	lw	R13, -8(R2)	% 
	putc	R13	% 
	hlt	% ======end of program======

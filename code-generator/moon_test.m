f11496724653	sw	4(R2), R3	% store return address
	lw	R13, 8(R2)	% load p1
	addi	R13, R13, 1	% p1+ 1
	sw	-4(R2), R13	% save to x
	lw	R7, -4(R2)	% 
	add	R3, R0, R7	% return value is a register value, get its value
	lw	R15, 4(R2)	% get return address
	lw	R2, 0(R2)	% load the previous frame pointer address
	addi	R1, R1, 16	% reset the stack pointer
	jr	R15	% 
	entry	% ======program entry======
	align	% following instruction align
	addi	R1, R0, topaddr	% initialize the stack pointer
	addi	R2, R0, topaddr	% initialize the frame pointer
	subi	R1, R1, 8	% set the stack pointer to the top position of the stack
	addi	R13, R0, 64	% 
	sw	-8(R2), R13	%  assign to x
	lw	R7, -8(R2)	% 
	sw	-4(R1), R7	% add parameter
	sw	-12(R1), R2	% store the previous frame pointer
	addi	R2, R1, -12	% update the frame pointer
	addi	R1, R2, -4	% update the stack pointer
	jl	R3, f11496724653	% store return address and jump to f11496724653
	sw	-4(R2), R3	% 
	lw	R15, -4(R2)	% 
	putc	R15	% 
	hlt	% ======end of program
	
	program {
    int x;
    int y;
    x = 64;
    y = f1(x);
    put (y);
};

int f1(int p1) {
    int x;
    x = p1 + 1;
    return (x);
};
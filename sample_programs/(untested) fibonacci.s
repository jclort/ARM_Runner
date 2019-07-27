@author Habibullah Aslam
@ This calculates the nth Fibonacci number, where n is put in r1
@ and the final answer is put in r0.
@ The sequence is assumed to start like this: 1, 1, 2, 3, 5, 8, etc.
@ So for n = 0, we get 1. For n=1, we get 1. For n = 2, we get 2. For n = 5, we get 8.
 
.global main
main:	
	mov r0, #0 @ the answer at the end of the execution
	mov r1, #5 @ n will be in this register
	bl fib
	swi 0x11   @ the end of the program
	
fib:
	sub sp, sp, #8
	str lr, [sp, #0]
	str r1, [sp, #4]
	
	cmp r1, #1
	bhi Else @ if n is > 1, branch
		
	add r0, r0, #1 @ we come here if n = 0 or 1, and then
	               @ we add 1 to the total so far 
	               @ (ie like "return 1" in C)
	add sp, sp, #8	
	mov pc, lr
	
Else:
	sub r1, r1, #1
	bl fib         @ call fib(n-1)
	sub r1, r1, #1
	bl fib         @ call fib(n-2)
	
	ldr r1, [sp, #4]
	ldr lr, [sp, #0]
	add sp, sp, #8
	
	mov pc, lr
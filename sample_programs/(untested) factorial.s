@author Habibullah Aslam
@ This does the factorial for the number on line 6. 

.global main
main:
  MOV r1, #5
fact:	
	sub sp, sp, #8
	str lr, [sp, #0]	;store lr into the stack pointer
	str r1, [sp, #4]
	cmp r1, #1
	bge Else
	mov r1, #1
	add sp, sp, #8		;add 8 to stack pointer
	mov pc, lr
Else:
	sub r1, r1, #1
	bl fact
	mov r2, r1
	ldr r1, [sp, #4]
	ldr lr, [sp, #0]
	add sp, sp, #8
	
	mul r1, r2, r1
	
	mov pc, lr
	swi 0x11			;Halt execution
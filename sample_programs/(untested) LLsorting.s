;@author Habibullah Aslam
;This is a hardcore ARM-32 program that reads ints from a file named "data.txt" in the same directory as this file, inserts them into a linked list (in sorted order), and then prints out the linked list. It works in ARMSim.
;Obviously ARM Runner cant do this, but maybe the next group that works on this project can implement the necessary things to make it work


.equ SWI_Open, 0x66		@ open a file 
.equ SWI_Close, 0x68		@ close a file
.equ SWI_PrStr, 0x69		@ Write a null-ending string 
.equ SWI_PrInt, 0x6b		@ Write an Integer 
.equ SWI_RdInt, 0x6c		@ Read an Integer from a file 
.equ Stdout, 1			@ Set output target to be Stdout 
.equ SWI_Exit, 0x11		@ Stop execution 
.equ SWI_AllocMem, 0x12	@ Allocate memory on the heap
.equ SWI_DeAllocHeap, 0x13	@ De-allocate the heap

@ Main method and insert functions authored by Habibullah Aslam

.global main
.text
main:
   mov r2, #0		@initialize r2, which will hold return values
   mov r3, #0		@initialize r3, which will hold read-in ints
   mov r4, #0		@initialize r4, which will hold pointer to next link
   mov r5, #0		@initialize r5, which will hold the return address of new pointers

   ldr r0, =InFileName
   mov r1, #0
   swi SWI_Open		@open InFileName  
   ldr r1, =InputFileHandle
   str r0, [r1]
   
   ldr r2, =root
   ldr r2, [r2]		@initialize root pointer to where root is pointing at
ReadIntLoop:   
   ldr r0, =InputFileHandle
   ldr r0, [r0]
   swi SWI_RdInt	@reads int
   bcs InitPrint	@if file empty then print out the LL
   mov r3, r0		@int read from file, put in r3   
   bl insert
   b ReadIntLoop
   
InitPrint:
   ldr r0, =root
   str r2, [r0]
   ldr r2, =root
   b print
   
insert:
   sub sp, sp, #4
   str lr, [sp, #0]	

if:  
   cmp r2, #0		@if root is null
   bne elseif
   
   mov r0, #8
   swi SWI_AllocMem	@malloc 8 bytes
   
   str r3, [r0, #0]	@store the data value in r3 into address at r0
   str r2, [r0, #4]	@store the address to the next pointer (in r2) into r0+4
   mov r5, r0		@store return val in r5
   mov r2, r5		@set root = *p
   
   mov pc, lr
   
elseif:
   ldr r6, [r2]
   cmp r3, r6
   bhs else
   
   mov r0, #8
   swi SWI_AllocMem	@malloc 8 bytes
   
   str r3, [r0, #0]	@store the data value in r3 into address at r0
   str r2, [r0, #4]	@store the address to the next pointer (in r2) into r0+4
   mov r5, r0		@store return val in r5
   mov r2, r5		@store return val in r2
   
   mov pc, lr
   
else:
   sub sp, sp, #8
   str r2, [sp, #4]	@store list pointer on stack
   str lr, [sp, #0]	@store loc here onto stack to come back later for putting pointer into list->next
   ldr r2, [r2, #4]	@put list->next into r2
   bl if
   
   ldr r4, [sp, #4] 	@get address of list back
   str r2, [r4, #4]	@put return value into list->next
   ldr r2, [sp, #4]	@put list pointer into r2
   
   ldr lr, [sp]		@get the lr from the stack
   add sp, sp, #8	@move the stack pointer up
   
   mov pc, lr   	@return to previous loc with proper registers set
   
print:
   ldr r2, =root
   ldr r2, [r2]		@load root node into r2
while:
   cmp r2, #0
   beq end
   ldr r1, [r2, #0]
   mov r0, #Stdout   
   swi SWI_PrInt	@print out value at the node
   ldr r1, =space
   swi SWI_PrStr	@print out a space
   
   ldr r2, [r2, #4]	@switch to next pointer
   b while   
end:
   swi SWI_Close
   swi SWI_DeAllocHeap
   swi SWI_Exit
   
.data
   root: .word 0
   InFileName: .asciz "data.txt"
   InputFileHandle: .word 0
   space: .asciz " "
.end
;@author Habibullah Aslam
;This is a hardcore ARM-32 program that takes a hex floating point in the data segment and multiplies it by 16. It works in ARMSim.
;Obviously ARM Runner cant do this, but maybe the next group that works on this project can implement the necessary things to make it work
;
;I picked a random number 0xc4650000 which is -916.0000 in floating point and -1000013824 as a signed 2s-complement int. 
;The program outputs -966459392, which is 0xC6650000 in hex, which is -14656.000000 as a floating point number.
;-916 * 16 = -14656, which is good!

.data
   x: .word 0xc4650000		@put any floating point number here you want
   space: .asciz " "
   
.text
.global main
main:
   mov r0, #1			@set flag for stdout with command 0x6b
   
   ldr r2, =x			@load address of x into r2
   ldr r1, [r2]			@load value at x into r1
   swi 0x6b			@print out the original x (at r1)
   
   ldr r1, =space
   swi 0x69			@print out a space
   
   ldr r1, =x			@load address of x into r1
   ldr r2, [r1] 		@load in the floating point number
 
   and r3, r2, #0x80000000	@put sign bit in r3

   ldr r4, =0x7f800000
   and r4, r2, r4		@put exponent in r4
   
   ldr r5, =0x007fffff
   and r5, r2, r5		@put fraction in r5
   
   add r5, r5, #0x02000000	@multiply number by 16 by adding 4 to exponent
   
   orr r6, r3, r6
   orr r6, r4, r6
   orr r6, r5, r6		@r6 now has the new number, 
				@which is the old number times 16 in IEEE 754 form
   
   str r6, [r1]			@store the answer back into x
   
   ldr r2, =x			@load address of x into r2
   ldr r1, [r2]			@load value at x into r1
   mov r0, #1
   
   swi 0x6b			@prints out the correct hex answer 
				@but reads it as a signed int rather than a float
   
   swi 0x11			@terminate execution
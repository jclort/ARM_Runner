.data
	str: .asciz "Hello World!\n"
.text			;2 byte alignment (can ignore for arm runner I guess)
.global main		;tells arm where to start the program? Its in all my notes
main:
	mov x0,#1		;sets the file handle for swi, #1 represents stdout
	ldr x1,=str 	;loads address of memory location of str
	swi 0x69		;print to console
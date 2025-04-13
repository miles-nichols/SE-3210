**PA1:**
The emulator is on Pyrite (ssh to pyrite.cs.iastate.edu) at /share/cs321/legv8emul.  It can be executed in place there.  You don't need to copy it.

 As part of a team project for a systems programming course, I implemented the <em>heapsort</em> algorithm entirely in LEGv8 assembly language, working closely with a partner to tackle low-level memory and register management. This assignment was designed to reinforce our understanding of ARMv8 architecture and calling conventions while developing a modular, efficient sorting algorithm without relying on nested loops.<br><br>

  I contributed to designing and coding multiple modular procedures, each adhering strictly to the ARMv8 calling conventions. These included custom memory access, stack frame setup, and careful register allocation to preserve program state. We wrote unit tests and used the provided in-house <code>legv8emul</code> emulator on a Linux-based remote server (Pyrite) to verify functionality and correctness. Debugging involved using emulator-specific pseudo-instructions like <code>PRNT</code>, <code>DUMP</code>, and <code>HALT</code>, as traditional debugging tools were unavailable.<br><br>

  The emulator environment simulated memory and stack separately, emphasizing safe memory management and realistic hardware constraints. We managed main memory directly using byte-level addressing, handled 8-byte integers with precision, and carefully tested each function in isolation before integration to avoid cascading bugs.<br><br>

  This project not only sharpened my skills in assembly language and low-level systems thinking but also improved my ability to work in a collaborative environment under strict submission and formatting guidelines. The experience deepened my understanding of computer architecture and reinforced best practices for memory-safe, procedure-driven programming in a constrained environment.

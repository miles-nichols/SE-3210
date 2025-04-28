package disassembler;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The LEGv8Disassembler class reads a file containing LEGv8 machine code,
 * decodes them, and prints the disassembled instructions with labels. The Emulator creates
 * a program.legv8asm.machine file containing the binary code. The disassembler reads this file
 * and decodes the instructions in a LEGv8 program that is run-able in the emulator.
 */
public class LEGv8Disassembler {

    private static int count = 1; // Instruction count for labeling

    /**
     * Reads LEGv8 instructions from a file and returns them as an array of integers.
     *
     * @param filename The name of the file containing LEGv8 instructions.
     * @return An array of integers representing the instructions.
     * @throws IOException If an error occurs while reading the file.
     */
    public static int[] readBinaryInstructions(String filename) throws IOException {
        // Read all bytes while ensuring proper file closure
        byte[] bytes;
        try (InputStream is = Files.newInputStream(Paths.get(filename))) {
            bytes = is.readAllBytes();
        }

        // Validate length
        if (bytes.length % 4 != 0) {
            throw new IOException("File size must be multiple of 4 bytes (32-bit instructions)");
        }

        // Convert to 32-bit instructions with proper endianness
        int[] instructions = new int[bytes.length / 4];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);

        IntBuffer ib = bb.asIntBuffer();
        ib.get(instructions);

        // Debug: Print first few instructions
        System.out.println("First 5 interpreted instructions:");
        for (int i = 0; i < Math.min(5, instructions.length); i++) {
            System.out.printf("%08X ", instructions[i]);
        }
        System.out.println();

        return instructions;
    }

    /**
     * Main method to run the disassembler.
     *
     * @param args Command line arguments, expecting the input file name.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java LEGv8FileReader <input_file>");
            return;
        }

        try {
            int[] program = readBinaryInstructions(args[0]);
            List<Instruction> instructionList = new ArrayList<>(); // Store all instructions

            for (int i = 0; i < program.length; i++) {
                Instruction instruction = new Instruction(program[i], count);
                InstructionDecoder decoder = new InstructionDecoder(instruction);
                decoder.decode(instruction, count);
                instructionList.add(instruction); // Store the instruction
                count++;
            }

            // Second pass, print with labels
            for (Instruction instr : instructionList) {
                // Print label if this is a branch target
                boolean isBranchTarget = instructionList.stream().anyMatch(i -> i.getBranchTarget() == instr.getCount());
                if (isBranchTarget) {
                    System.out.println(instr.getLabel() + ":");
                }
                System.out.println("\t" + instr.getPrintedInstruction());
            }
                  } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
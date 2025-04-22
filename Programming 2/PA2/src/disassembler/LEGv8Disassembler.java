package disassembler;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class LEGv8Disassembler {

    private static int count = 1;

    public static int[] readInstructionsFromFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
        List<Integer> instructions = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) {
                continue; // Skip empty lines and comments
            }

            // Extract first token (ignore binary representations)
            String hexStr = line.split("\\s+")[0].trim();

            try {
                // Validate hex length (8 chars for 32-bit instructions)
                if (hexStr.length() != 8) {
                    System.err.println("Skipping invalid instruction length: " + line);
                    continue;
                }
                // Parse as unsigned 32-bit value
                long value = Long.parseLong(hexStr, 16);
                if (value > 0xFFFFFFFFL) {
                    System.err.println("Skipping out-of-range value: " + line);
                    continue;
                }

                instructions.add((int)value);
            } catch (NumberFormatException e) {
                System.err.println("Skipping malformed hex: " + line);
            }
        }
        return instructions.stream().mapToInt(i -> i).toArray();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java LEGv8FileReader <input_file>");
            return;
        }

        try {
            int[] program = readInstructionsFromFile(args[0]);
            List<Instruction> instructionList = new ArrayList<>(); // Store all instructions

            for (int i = 0; i < program.length; i++) {
                Instruction instruction = new Instruction(program[i], count);
                InstructionDecoder decoder = new InstructionDecoder(instruction);
                decoder.decode(instruction, count);
                instructionList.add(instruction); // Store the instruction
                count++;
            }

            // Second pass: print with labels
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
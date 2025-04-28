package disassembler;

/**
 * The InstructionDecoder class is responsible for decoding LEGv8 instructions.
 * It takes an instruction and decodes it into its components, matching it against
 * a predefined set of LEGv8 instructions.
 */
public class InstructionDecoder {

    // LEGv8 required instruction set
    public LEGv8Instruction[] instructions = {
            new LEGv8Instruction("ADD", 0b10001011000, "R"),
            new LEGv8Instruction("ADDI", 0b1001000100, "I"),
            new LEGv8Instruction("SUB", 0b11001011000, "R"),
            new LEGv8Instruction("SUBI", 0b1101000100, "I"),
            new LEGv8Instruction("SUBIS", 0b1111000100, "I"),
            new LEGv8Instruction("SUBS", 0b11101011000, "R"),
            new LEGv8Instruction("MUL", 0b10011011000, "R"),
            new LEGv8Instruction("AND", 0b10001010000, "R"),
            new LEGv8Instruction("ANDI", 0b1001001000, "I"),
            new LEGv8Instruction("ORR", 0b10101010000, "R"),
            new LEGv8Instruction("ORRI", 0b1011001000, "I"),
            new LEGv8Instruction("EOR", 0b11001010000, "R"),
            new LEGv8Instruction("EORI", 0b1101001000, "I"),
            new LEGv8Instruction("LSL", 0b11010011011, "R"),
            new LEGv8Instruction("LSR", 0b11010011010, "R"),
            new LEGv8Instruction("LDUR", 0b11111000010, "D"),
            new LEGv8Instruction("STUR", 0b11111000000, "D"),
            new LEGv8Instruction("B", 0b000101, "B"),
            new LEGv8Instruction("B.EQ", 0b01010100, "CB"),    // 0
            new LEGv8Instruction("B.NE", 0b01010100, "CB"),    // 1
            new LEGv8Instruction("B.HS", 0b01010100, "CB"),    // 2
            new LEGv8Instruction("B.LO", 0b01010100, "CB"),    // 3
            new LEGv8Instruction("B.MI", 0b01010100, "CB"),    // 4
            new LEGv8Instruction("B.PL", 0b01010100, "CB"),    // 5
            new LEGv8Instruction("B.VS", 0b01010100, "CB"),    // 6
            new LEGv8Instruction("B.VC", 0b01010100, "CB"),    // 7
            new LEGv8Instruction("B.HI", 0b01010100, "CB"),    // 8
            new LEGv8Instruction("B.LS", 0b01010100, "CB"),    // 9
            new LEGv8Instruction("B.GE", 0b01010100, "CB"),    // a
            new LEGv8Instruction("B.LT", 0b01010100, "CB"),    // b
            new LEGv8Instruction("B.GT", 0b01010100, "CB"),    // c
            new LEGv8Instruction("B.LE", 0b01010100, "CB"),    // d
            new LEGv8Instruction("BL", 0b100101, "B"),
            new LEGv8Instruction("BR", 0b11010110000, "R"),
            new LEGv8Instruction("CBNZ", 0b10110101, "CB"),
            new LEGv8Instruction("CBZ", 0b10110100, "CB"),
            new LEGv8Instruction("PRNT", 0b11111111101, "R"),
            new LEGv8Instruction("PRNL", 0b11111111100, "R"),
            new LEGv8Instruction("DUMP", 0b11111111110, "R"),
            new LEGv8Instruction("HALT", 0b11111111111, "R")
    };

    private Instruction instruction; // the instruction to decode
    private int opcode6; // if opcode is 6 bits
    private int opcode8; // if opcode is 8 bits
    private int opcode10; // if opcode is 10 bits
    private int opcode11; // if opcode is 11 bits
    private int RdRt; // if not B type, 5 most right bits
    private int Rm; // R type, bits 20-16
    private int Rn; // R, I, or D type; bits 9 - 5
    private int shamt; // R type, bits 15-10;
    private int ALU_imm; // I type, bits 20-10
    private int BRaddress; // B type, 25-0
    private int CondBRaddress; // CB bits 23-5
    private int DTaddress; // d type 20-12
    private int op; // d type, 11-10
    private LEGv8Instruction rightInst; // holds the correct instruction

    public InstructionDecoder(Instruction instruction) {
        this.instruction = instruction;
    }

    /**
     * Decodes the instruction by extracting the opcode and calling the matchOpcode method.
     * @param instruction
     * @param count
     */
    public void decode(Instruction instruction, int count) {
        //get possible opcodes
        opcode6 = (instruction.getValue() >>> 26) & 0b111111;    // 6 bits
        opcode8 = (instruction.getValue() >>> 24) & 0b11111111;   // 8 bits
        opcode10 = (instruction.getValue() >>> 22) & 0b1111111111; // 10 bits
        opcode11 = (instruction.getValue() >>> 21) & 0b11111111111; // 11 bits
        instruction.setLabel(count);
        matchOpcode();
    }

    /**
     * Matches the opcode with the LEGv8 instruction set and calls the buildInstruction method.
     */
    public void matchOpcode() {
        for (LEGv8Instruction inst : instructions) {
            if (inst.getOpcode() == opcode6 || inst.getOpcode() == opcode8 || inst.getOpcode() == opcode10 || inst.getOpcode() == opcode11) {
                rightInst = inst;
                buildInstruction();
                return;
            }
        }
        System.out.println("No matching instruction found.");
    }

    /**
     * Builds the instruction string based on the decoded components.
     */
    public void buildInstruction() {
        Rm = (instruction.getValue() >>> 16) & 0b11111; //get 20-16 bits by putting bit 16 on the LSB and getting the first 5
        shamt = (instruction.getValue() >>> 10) & 0b111111; //get bits 15-10 by putting bit 10 on the LSB and getting the first 6
        Rn = (instruction.getValue() >>> 5) & 0b11111; // get bits 9-5 by put bit 5 on the LSB and get the first 5
        RdRt = instruction.getValue() & 0b11111; // get 5 rightmost bits by getting the first 5
        ALU_imm = (instruction.getValue() >>> 10) & 0b11111111111; // get bits 21-10 by putting bit 10 on the LSB and getting the first 11
        DTaddress = (instruction.getValue() >>> 12) & 0b111111111; // get bits 20-12 by putting bit 12 on the LSB and getting the first 9
        op = (instruction.getValue() >>> 10) & 0b11; // get bits 11-10 by putting bit 10 on the LSB and getting the first 2
        BRaddress = instruction.getValue() & 0b11111111111111111111111111; // get bits 25-0 by putting bit 0 on the LSB and getting the first 26
        CondBRaddress = (instruction.getValue() >>> 5) & 0b1111111111111111111; // get bits 23-5 by putting bit 5 on the LSB and getting the first 19

        switch (rightInst.getType()) { // check type and print instruction accordingly
            case "R":
                if (rightInst.getName().equals("LSL") || rightInst.getName().equals("LSR")) {
                    instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt + ", X" + Rn + ", #" + shamt);
                } else if (rightInst.getName().equals("PRNT")) {
                    instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt);
                } else if (rightInst.getName().equals("PRNL") || rightInst.getName().equals("DUMP") || rightInst.getName().equals("HALT")) {
                    instruction.setPrintedInstruction(rightInst.getName());
                } else if(rightInst.getName().equals("BR")){
                    instruction.setPrintedInstruction(rightInst.getName() + " X" + Rn);
                } else {
                    instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt + ", X" + Rn + ", X" + Rm);
                }
                break;
            case "I":
                instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt + ", X" + Rn + ", #" + ALU_imm);
                break;
            case "D":
                instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt + ", [X" + Rn + ", #" + DTaddress + "]");
                break;
            case "B":
                int imm26 = instruction.getValue() & 0x03FFFFFF;  // Extract 26-bit offset
                imm26 = (imm26 << 6) >> 6;  // Sign-extend to 32 bits
                int targetCount = instruction.getCount() + imm26;
                instruction.setBranchTarget(targetCount);
                instruction.setBranchTaken(true);
                instruction.setPrintedInstruction(rightInst.getName() + " label" + targetCount);
                break;
            case "CB":
                int imm19 = (instruction.getValue() >>> 5) & 0x7FFFF;  // Extract 19-bit offset
                imm19 = (imm19 << 13) >> 13;  // Sign-extend to 32 bits
                int cbTargetCount = instruction.getCount() + imm19;
                instruction.setBranchTarget(cbTargetCount);
                instruction.setBranchTaken(true);
                if (rightInst.getName().startsWith("B.")) {
                    String condName = getConditionName(instruction.getValue());
                    instruction.setPrintedInstruction(condName + " label" + cbTargetCount);
                } else {
                    instruction.setPrintedInstruction(rightInst.getName() + " X" + RdRt + ", label" + cbTargetCount);
                }
                break;
        }
    }

    /**
     * Returns the condition name based on the instruction value.
     * @param instructionValue
     * @return condition name
     */
    private String getConditionName(int instructionValue) {
        int condition = instruction.getValue() & 0b1111; // Extract bits [3:0]
        switch (condition) {
            case 0b0000:
                return "B.EQ";
            case 0b0001:
                return "B.NE";
            case 0b0010:
                return "B.HS";
            case 0b0011:
                return "B.LO";
            case 0b0100:
                return "B.MI";
            case 0b0101:
                return "B.PL";
            case 0b0110:
                return "B.VS";
            case 0b0111:
                return "B.VC";
            case 0b1000:
                return "B.HI";
            case 0b1001:
                return "B.LS";
            case 0b1010:
                return "B.GE";
            case 0b1011:
                return "B.LT";
            case 0b1100:
                return "B.GT";
            case 0b1101:
                return "B.LE";
            default:
                return "B.UNKNOWN";
        }
    }
}

package disassembler;

/**
 * Instruction class represents a single instruction in the disassembler.
 * It contains information about the instruction type, label, value, and other properties.
 */
public class Instruction {
    private String type;
    private String label;
    private int value;
    private boolean branchTaken;
    private int branchTarget;
    private String printedInstruction;
    private int count;

    public Instruction(int value, int count) {
        this.value = value;
        this.type = "";
        this.label = "";
        this.branchTaken = false;
        this.branchTarget = 0;
        this.printedInstruction = "";
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
       return label;
    }

    public void setLabel(int count) {
        this.label = "label" + count;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setBranchTarget(int target) {
        this.branchTarget = target;
    }

    public int getBranchTarget() {
        return branchTarget;
    }

    public String getPrintedInstruction() {
        return printedInstruction;
    }
    public void setPrintedInstruction(String printedInstruction) {
        this.printedInstruction = printedInstruction;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public boolean isBranchTaken() {
        return branchTaken;
    }
    public void setBranchTaken(boolean taken) {
        this.branchTaken = taken;
    }
}
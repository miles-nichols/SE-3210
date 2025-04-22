package disassembler;

public class LEGv8Instruction {

    private String name;
    private int opcode;
    private String type;

    public LEGv8Instruction(String name, int opcode, String type) {
        this.name = name;
        this.opcode = opcode;
        this.type = type;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}

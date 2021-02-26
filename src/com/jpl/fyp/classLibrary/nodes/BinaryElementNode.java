package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class BinaryElementNode extends ExpressionElementNode
{
    private ExpressionElementNode leftSide;
    private ExpressionElementNode rightSide;

    public BinaryElementNode(Token token,
                             Token[] rightSide,
                             Token[] leftSide) throws JPLException {
        super(token);
        this.leftSide = ExpressionParser.parse(leftSide);
        this.rightSide = ExpressionParser.parse(rightSide);
    }

	public ExpressionElementNode getLeftSide() {
		return leftSide;
	}

	public ExpressionElementNode getRightSide() {
		return rightSide;
	}

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        this.leftSide.validate(entries);
        this.rightSide.validate(entries);
    }

    @Override
    public String toString() {
        return "Binary Element Node:\n" +
            "Type : " + getToken().tokenType + "\n" +
            "{\n" +
            "Left Side:\n" +
            "{\n" +
            leftSide.toString() + "\n" +
            "}\n" +
            "Right Side:\n" +
            "{\n" +
            rightSide.toString() + "\n" +
            "}\n" +
            "}\n";
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(this.leftSide.generateIntermediateCode());
        instructions.addAll(this.rightSide.generateIntermediateCode());
        instructions.add(this.generateInstructionForCurrentNode());
        return instructions;
    }

	private IntermediateCodeInstruction generateInstructionForCurrentNode() throws JPLException {
        IntermediateCodeInstructionType operator = this.getInstructionType();
		String leftArgument = generateSideArgument(this.leftSide, 1);
        String rightArgument = generateSideArgument(this.rightSide, 2);
        String result = IntermediateCodeInstruction.getNewTemporaryVariableName();
        return new IntermediateCodeInstruction(operator, leftArgument, rightArgument, result);
	}

	private String generateSideArgument(ExpressionElementNode node, int offset) throws JPLException {
		String result;
		if (node instanceof ValueElementNode) {
            result = node.getToken().tokenValue;
        } else if (node instanceof BinaryElementNode) {
            int currentTemporaryVariableIndex = IntermediateCodeInstruction.getCurrentTemporaryVariableIndex();
            result = IntermediateCodeInstruction.IntegerToInstructionName(currentTemporaryVariableIndex - offset);
        } else {
            throw new JPLException("BinaryElementNode : unhandled ElementNode type.");
        }
		return result;
	}

	private IntermediateCodeInstructionType getInstructionType() throws JPLException {
        TokenType tokenType = this.getToken().tokenType;
        IntermediateCodeInstructionType convertedInstructionType;
        switch (tokenType) {
            case Add: {
                convertedInstructionType = IntermediateCodeInstructionType.Add;
                break;
            }
            case Subtract: {
                convertedInstructionType = IntermediateCodeInstructionType.Subtract;
                break;
            }
            case Multiply: {
                convertedInstructionType = IntermediateCodeInstructionType.Multiply;
                break;
            }
            case Divide: {
                convertedInstructionType = IntermediateCodeInstructionType.Divide;
                break;
            }
            case Or: {
                convertedInstructionType = IntermediateCodeInstructionType.Or;
                break;
            }
            case And: {
                convertedInstructionType = IntermediateCodeInstructionType.And;
                break;
            }
            case Equal: {
                convertedInstructionType = IntermediateCodeInstructionType.Equal;
                break;
            }
            case NotEqual: {
                convertedInstructionType = IntermediateCodeInstructionType.NotEqual;
                break;
            }
            case GreaterThan: {
                convertedInstructionType = IntermediateCodeInstructionType.GreaterThan;
                break;
            }
            case LessThan: {
                convertedInstructionType = IntermediateCodeInstructionType.LessThan;
                break;
            }
            case GreaterThanOrEqualTo: {
                convertedInstructionType = IntermediateCodeInstructionType.GreaterThanOrEqualTo;
                break;
            }
            case LessThanOrEqualTo: {
                convertedInstructionType = IntermediateCodeInstructionType.LessThanOrEqualTo;
                break;
            }
            default: {
                throw new JPLException("Binary Element Node : invalid tokenType.");
            }
        }
        return convertedInstructionType;
	}
}

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
    private ExpressionElementNode leftExpressionElementNode;
    private ExpressionElementNode rightExpressionElementNode;

    public BinaryElementNode(Token token,
                             Token[] rightSide,
                             Token[] leftSide) throws JPLException {
        super(token);
        this.leftExpressionElementNode = ExpressionParser.parse(leftSide);
        this.rightExpressionElementNode = ExpressionParser.parse(rightSide);
    }

	public ExpressionElementNode getLeftSide() {
		return leftExpressionElementNode;
	}

	public ExpressionElementNode getRightSide() {
		return rightExpressionElementNode;
	}

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        this.leftExpressionElementNode.validate(entries);
        this.rightExpressionElementNode.validate(entries);
    }

    @Override
    public String toString() {
        return "Binary Element Node:\n" +
            "Type : " + getToken().tokenType + "\n" +
            "{\n" +
            "Left Side:\n" +
            "{\n" +
            leftExpressionElementNode.toString() + "\n" +
            "}\n" +
            "Right Side:\n" +
            "{\n" +
            rightExpressionElementNode.toString() + "\n" +
            "}\n" +
            "}\n";
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        ExpressionElementNode leftExpression = this.leftExpressionElementNode;
        ExpressionElementNode rightExpression = this.rightExpressionElementNode;
        ArrayList<IntermediateCodeInstruction> leftExpressionIntermediateCode = leftExpression.generateIntermediateCode();
        ArrayList<IntermediateCodeInstruction> rightExpressionIntermediateCode = rightExpression.generateIntermediateCode();

        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(leftExpressionIntermediateCode);
        instructions.addAll(rightExpressionIntermediateCode);
        instructions.add(new IntermediateCodeInstruction(this.getInstructionType(),
                                                         this.generateSideArgument(leftExpression,
                                                                                   leftExpressionIntermediateCode),
                                                         this.generateSideArgument(rightExpression,
                                                                                   rightExpressionIntermediateCode),
                                                         IntermediateCodeInstruction.getNewTemporaryVariableName()));
        return instructions;
    }

	private String generateSideArgument(ExpressionElementNode node,
                                        ArrayList<IntermediateCodeInstruction> intermediateCode) throws JPLException {
		if (node instanceof ValueElementNode) {
            return node.getToken().tokenValue;
        } else if (node instanceof BinaryElementNode
                   ||
                   node instanceof FunctionElementNode) {
            return intermediateCode.get(intermediateCode.size() -1).getResult();
        } else {
            throw new JPLException("BinaryElementNode : unhandled ElementNode type.");
        }
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

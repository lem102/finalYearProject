package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class IfNode extends ConditionalNode
{
    public ExpressionNode testExpression;

    public IfNode(Token[] tokens)
        throws JPLException
    {
        this.validateTokens(tokens);

        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 2);
        this.testExpression = new ExpressionNode(expressionTokens);
    }

	private void validateTokens(Token[] tokens)
        throws JPLException
    {
        if (tokens[0].tokenType != TokenType.If)
        {
            throw new JPLException("If Statement : First token must be an if identifier.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            throw new JPLException("If Statement : Second token must be an opening parenthesis.");
        }
        else if (tokens[tokens.length-2].tokenType != TokenType.ClosingParenthesis)
        {
            throw new JPLException("If Statement : Second to last token must be a closing parenthesis.");
        }
        else if (tokens[tokens.length-1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("If Statement : Last token must be an opening brace.");
        }
	}

	@Override
	public String toString() {
        return "If Statement:\n"
            + "(\n"        
            + "Expression:\n"
            + testExpression
            + ")\n"
            + super.toString()
            + "Else:\n"
            + "{\n"
            + super.getElseNode() + "\n"
            + "}\n";
	}

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(this.generateIfStatementInstructions());

        if (this.getElseNode() != null) {
            String endLabel = IntermediateCodeInstruction.getNewLabelName();

            IntermediateCodeInstruction gotoEndInstruction = this.generateGotoInstruction(endLabel);
            instructions = this.insertGotoEndInstruction(instructions, gotoEndInstruction);
            IntermediateCodeInstruction endLabelInstruction = this.generateLabelInstruction(endLabel);
            
            instructions.addAll(this.getElseNode().generateIntermediateCode(endLabel));
            instructions.add(endLabelInstruction);
        }
        
        return instructions;
    }

	public ArrayList<IntermediateCodeInstruction> insertGotoEndInstruction(ArrayList<IntermediateCodeInstruction> instructions, IntermediateCodeInstruction gotoEndInstruction) {
        instructions.add(instructions.size() -1, gotoEndInstruction);
		return instructions;
	}

	public ArrayList<IntermediateCodeInstruction> generateIfStatementInstructions() throws JPLException {
        ArrayList<IntermediateCodeInstruction> testExpressionInstructions = this.testExpression.generateIntermediateCode();
        String expressionResultVariableName = super.getExpressionResultVariableName(this.testExpression.getRootExpressionElementNode().getToken(), testExpressionInstructions);
        String label = IntermediateCodeInstruction.getNewLabelName();
        IntermediateCodeInstruction conditionalGotoInstruction = super.generateConditionalGotoInstructions(label, expressionResultVariableName);
        ArrayList<IntermediateCodeInstruction> statementInstructions = super.generateStatementInstructions();
        IntermediateCodeInstruction labelInstruction = super.generateLabelInstruction(label);
        
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(testExpressionInstructions);
        instructions.add(conditionalGotoInstruction);
        instructions.addAll(statementInstructions);
        instructions.add(labelInstruction);

        return instructions;
	}
}

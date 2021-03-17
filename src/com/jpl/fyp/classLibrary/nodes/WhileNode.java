package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class WhileNode extends ContainingNode
{
    public ExpressionNode testExpression;

    public WhileNode(Token[] tokens) throws JPLException
    {
        validateTokens(tokens);
        Token[] expressionTokens = Arrays.copyOfRange(tokens, 2, tokens.length - 2);
        this.testExpression = new ExpressionNode(expressionTokens);
        super.setStatements(new ArrayList<StatementNode>());
    }

	private void validateTokens(Token[] tokens) throws JPLException
    {
        if (tokens[0].tokenType != TokenType.While)
        {
            throw new JPLException("While Node : First token is not while keyword.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningParenthesis)
        {
            throw new JPLException("While Node : Second token is not an opening parenthesis.");
        }
        else if (tokens[tokens.length - 2].tokenType != TokenType.ClosingParenthesis)
        {
            throw new JPLException("While Node : Second to last token is not a closing parenthesis.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("While Node : Last token is not an opening brace.");
        }
	}

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        this.testExpression.validate(entries);
    }
    
	@Override
    public String toString() {
        return "While Loop:\n"
            + "(\n"
            + "Expression:\n"
            + testExpression
            + ")\n"
            + super.toString();
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        ArrayList<IntermediateCodeInstruction> testExpressionInstructions = this.testExpression.generateIntermediateCode();
        String expressionResultVariableName = super.getExpressionResultVariableName(this.testExpression.getRootExpressionElementNode().getToken(), testExpressionInstructions);
        String preLoopLabel = IntermediateCodeInstruction.getNewLabelName();
        String postLoopLabel = IntermediateCodeInstruction.getNewLabelName();
        IntermediateCodeInstruction exitLoopConditionalGotoInstruction = super.generateConditionalGotoInstructions(postLoopLabel, expressionResultVariableName);
        ArrayList<IntermediateCodeInstruction> statementInstructions = super.generateStatementInstructions();
        IntermediateCodeInstruction preLoopLabelInstruction = super.generateLabelInstruction(preLoopLabel);
        IntermediateCodeInstruction postLoopLabelInstruction = super.generateLabelInstruction(postLoopLabel);
        IntermediateCodeInstruction loopingGotoInstruction = super.generateGotoInstruction(preLoopLabel);

        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.add(preLoopLabelInstruction);
        instructions.addAll(testExpressionInstructions);
        instructions.add(exitLoopConditionalGotoInstruction);
        instructions.addAll(statementInstructions);
        instructions.add(loopingGotoInstruction);
        instructions.add(postLoopLabelInstruction);
        
        return instructions;
    }
}

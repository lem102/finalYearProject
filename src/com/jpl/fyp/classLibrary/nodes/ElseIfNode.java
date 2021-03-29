package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ElseIfNode extends IfNode {
	public ElseIfNode(Token[] tokens) throws JPLException {
        super(prepareTokens(tokens));
	}

	private static Token[] prepareTokens(Token[] tokens) throws JPLException {
		if (tokens[0].tokenType != TokenType.Else) {
            throw new JPLException("Else If Node : First token must be an else token.");
        }

        Token[] preparedTokens = Arrays.copyOfRange(tokens, 1, tokens.length);

        return preparedTokens;
	}

    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode(String endLabel) throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(this.generateIfStatementInstructions());
        IntermediateCodeInstruction gotoEndInstruction = this.generateGotoInstruction(endLabel);
		instructions = this.insertGotoEndInstruction(instructions, gotoEndInstruction);
        
        if (this.getElseNode() != null) {
            instructions.addAll(this.getElseNode().generateIntermediateCode(endLabel));
        }
        return instructions;
    }
}

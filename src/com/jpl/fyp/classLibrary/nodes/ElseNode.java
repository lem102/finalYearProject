package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ElseNode extends ContainingNode
{
    public ElseNode(Token[] tokens) throws JPLException
    {
        super();
        this.validateTokens(tokens);
    }

    private void validateTokens(Token[] tokens) throws JPLException
    {
        if (tokens.length != 2)
        {
            throw new JPLException("Else Node : There should only be two tokens.");
        }
        else if (tokens[0].tokenType != TokenType.Else)
        {
            throw new JPLException("Else Node : First token must be an else token.");
        }
        else if (tokens[1].tokenType != TokenType.OpeningBrace)
        {
            throw new JPLException("Else Node : Second token must be an opening brace token.");
        }
	}

	@Override
	public String toString() {
        return "Else Node:\n"
            + super.toString();
	}

    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode(String endLabel) throws JPLException {
		var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(super.generateStatementInstructions());
		return instructions;
    }
}

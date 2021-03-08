package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.jpl.fyp.classLibrary.ExpressionInstructionInformation;
import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.JPLType;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class DeclarationNode extends StatementNode
{
    public JPLType type;

    public String name;

    public ExpressionNode expression;

    public DeclarationNode(Token[] tokens) throws JPLException {
        this.validateTokens(tokens);
        this.type = this.tokenToType(tokens[0]);
        this.name = tokens[1].tokenValue;

        if (tokens.length > 3)
        {
            Token[] expressionTokens = Arrays.copyOfRange(tokens, 3, tokens.length-1);
            this.expression = new ExpressionNode(expressionTokens);
        }

        setSymbolTableEntry(new SymbolTableEntry(type, name));
	}

	private JPLType tokenToType(Token token) throws JPLException {
        if (token.tokenType == TokenType.IntegerDeclaration) {
            return JPLType.Integer;
        } else {
            throw new JPLException("Declaration Node : Invalid type.");
        }
	}

	private void validateTokens(Token[] tokens)
        throws JPLException
    {
        if (tokens[0].tokenType != TokenType.IntegerDeclaration)
        {
            throw new JPLException("Declaration Node : First token must be a declaration token. Currently there are only integer declarations available.");
        }
        else if (tokens[1].tokenType != TokenType.Identifier)
        {
            throw new JPLException("Declaration Node : Second token must be an name.");
        }
        else if (tokens[2].tokenType != TokenType.Assignment && tokens[2].tokenType != TokenType.Semicolon)
        {
            throw new JPLException("Declaration Node : Third token must be either an assignment symbol or a semicolon.");
        }
        else if (tokens[tokens.length - 1].tokenType != TokenType.Semicolon)
        {
            throw new JPLException("Declaration Node : Last token must be a semicolon.");
        }
	}

	@Override
    public String toString()
    {
        String output = "";
        output += "Declaration:\n";
        output += "Type: " + type + "\n";
        output += "Name: " + name + "\n";
        output += "Expression:\n";
        output += expression + "\n";
        
        return output;
    }

    @Override
    public ArrayList<IntermediateCodeInstruction> generateIntermediateCode() throws JPLException {
        ExpressionInstructionInformation argumentInstructionInformation = new ExpressionInstructionInformation(this.expression);
        ArrayList<IntermediateCodeInstruction> expressionIntermediateCode = argumentInstructionInformation.getExpressionInstructions();
        String expressionResult = argumentInstructionInformation.getExpressionResultVariableName();
        IntermediateCodeInstruction declarationInstruction = this.generateDeclarationInstruction(expressionResult);

        var instructions = new ArrayList<IntermediateCodeInstruction>();
        instructions.addAll(expressionIntermediateCode);
        instructions.add(declarationInstruction);
        return instructions;
    }

	private IntermediateCodeInstruction generateDeclarationInstruction(String expressionResultVariableName) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.Declare,
                                               expressionResultVariableName,
                                               null,
                                               this.name);
	}
}

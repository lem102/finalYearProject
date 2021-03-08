package com.jpl.fyp.classLibrary;

import java.util.ArrayList;

import com.jpl.fyp.classLibrary.nodes.ExpressionNode;

public class ExpressionInstructionInformation {
	private ArrayList<IntermediateCodeInstruction> expressionInstructions = new ArrayList<IntermediateCodeInstruction>();
	private ArrayList<String> expressionResultVariableNames = new ArrayList<String>();
	private String expressionResultVariableName;

	public ExpressionInstructionInformation(ExpressionNode[] arguments) throws JPLException {
        for (ExpressionNode argument : arguments) {
            ArrayList<IntermediateCodeInstruction> argumentInstructions = argument.generateIntermediateCode();
            this.expressionInstructions.addAll(argumentInstructions);
            String argumentResultVariableName = this.getExpressionResult(argumentInstructions, argument);
            this.expressionResultVariableNames.add(argumentResultVariableName);
        }
	}

	public ExpressionInstructionInformation(ExpressionNode expression) throws JPLException {
        if (expression != null) {
            ArrayList<IntermediateCodeInstruction> expressionIntermediateCode = expression.generateIntermediateCode();
            this.expressionInstructions.addAll(expressionIntermediateCode);
            this.expressionResultVariableName = this.getExpressionResult(expressionIntermediateCode, expression);
        }
	}

	private String getExpressionResult(ArrayList<IntermediateCodeInstruction> expressionIntermediateCode, ExpressionNode expression) {
        if (expressionIntermediateCode.size() > 0) {
            return expressionIntermediateCode.get(expressionIntermediateCode.size() -1).getResult();
        } else {
            return expression.getRootExpressionElementNode().getToken().tokenValue;
        }
	}

	public ArrayList<IntermediateCodeInstruction> getExpressionInstructions() {
		return this.expressionInstructions;
	}

	public ArrayList<String> getExpressionResultVariableNames() {
		return this.expressionResultVariableNames;
	}
    
	public String getExpressionResultVariableName() {
		return expressionResultVariableName;
	}
}

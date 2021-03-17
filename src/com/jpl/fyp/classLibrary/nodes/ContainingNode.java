package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jpl.fyp.classLibrary.IntermediateCodeInstruction;
import com.jpl.fyp.classLibrary.IntermediateCodeInstructionType;
import com.jpl.fyp.classLibrary.JPLException;
import com.jpl.fyp.classLibrary.SymbolTable;
import com.jpl.fyp.classLibrary.SymbolTableEntry;
import com.jpl.fyp.classLibrary.Token;
import com.jpl.fyp.classLibrary.TokenType;

public class ContainingNode extends StatementNode {
    private List<StatementNode> statements;
    private SymbolTable symbolTable;

    public ContainingNode() {
        this.statements = new ArrayList<StatementNode>();
        this.symbolTable = new SymbolTable();
    }

	public List<StatementNode> getStatements() {
        return this.statements;
    }

	public void addStatement(StatementNode statement) {
        this.statements.add(statement);
	}

	public void setStatements(ArrayList<StatementNode> statements) {
        this.statements = statements;
	}

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

	public void addSymbolToTable(SymbolTableEntry symbolTableEntry) {
        this.symbolTable.addSymbol(symbolTableEntry);
	}

	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader) {
		return endOfHeader;
	}

    @Override
    public void validate(SymbolTableEntry[] entries) throws JPLException {
        entries = mergeEntries(entries);
        for (StatementNode statement : this.statements) {
            statement.validate(entries);
        }
    }

    private SymbolTableEntry[] mergeEntries(SymbolTableEntry[] parentEntriesArray) {
        List<SymbolTableEntry> currentNodeEntries = this.symbolTable.getSymbols();
        List<SymbolTableEntry> parentNodeEntries = Arrays.asList(parentEntriesArray);
        var entries = new ArrayList<SymbolTableEntry>();
        entries.addAll(currentNodeEntries);
        entries.addAll(parentNodeEntries);
        return entries.toArray(new SymbolTableEntry[entries.size()]);
    }

    @Override
    public String toString() {
        return this.symbolTable
            + statementsToString();
    }

	private String statementsToString() {
        String stringOfStatements = "Statements:\n"
            + "{\n";
        for (StatementNode statementNode : this.statements) {
            stringOfStatements += statementNode;
        }
        stringOfStatements += "}\n";
        return stringOfStatements;
    }

    public ArrayList<IntermediateCodeInstruction> generateStatementInstructions() throws JPLException {
        var instructions = new ArrayList<IntermediateCodeInstruction>();
        for (StatementNode statement : this.getStatements()) {
            instructions.addAll(statement.generateIntermediateCode());
        }
		return instructions;
	}
    
	public IntermediateCodeInstruction generateLabelInstruction(String label) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.Label,
                                               label,
                                               null,
                                               null);
	}

	public ArrayList<IntermediateCodeInstruction> generateIntermediateCode(String endLabel) throws JPLException {
        System.out.println("JACOB WARNING: should not call this method.");
        return new ArrayList<IntermediateCodeInstruction>();
	}

    
	public IntermediateCodeInstruction generateConditionalGotoInstructions(String label,
                                                                           String expressionResultVariableName) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.IfFalseGoto,
                                               expressionResultVariableName,
                                               null,
                                               label);
	}

	public IntermediateCodeInstruction generateGotoInstruction(String endLabel) {
		return new IntermediateCodeInstruction(IntermediateCodeInstructionType.Goto,
                                               null,
                                               null,
                                               endLabel);
	}
}

package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

import com.jpl.fyp.classLibrary.SymbolTable;
import com.jpl.fyp.classLibrary.SymbolTableEntry;

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

	@Override
	public int moveIndexToNextStatement(int endOfStatement, int endOfHeader) {
		return endOfHeader;
	}

	public void setStatements(ArrayList<StatementNode> statements) {
        this.statements = statements;
	}

	public void addSymbolToTable(SymbolTableEntry symbolTableEntry) {
        symbolTable.addSymbol(symbolTableEntry);
	}

    @Override
    public String toString() {
        return "Statements:\n"
            + "{\n"
            + statementsToString()
            + "}\n";
    }

    private String statementsToString() {
        String stringOfStatements = "";
        for (StatementNode statementNode : this.statements) {
            stringOfStatements += statementNode;
        }
        return stringOfStatements;
    }
}

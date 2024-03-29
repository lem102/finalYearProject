package com.jpl.fyp.classLibrary;

public enum IntermediateCodeInstructionType {
	Label,
    BeginFunction,
    EndFunction,
    Value,

    Add,
    Subtract,
    Multiply,
    Divide,

    Or,
    And,

    Equal,
    NotEqual,
    GreaterThan,
    LessThan,
    GreaterThanOrEqualTo,
    LessThanOrEqualTo,

    IfFalseGoto,
    Assign,
    Declare,
    Goto,
    LabelCall,
    PushParameter,
    PopParameter,

    Print,
    Return, 
}

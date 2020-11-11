package com.jpl.fyp.classLibrary.nodes;

import java.util.ArrayList;
import java.util.List;

public class RootNode
{
    public List<DefinitionNode> definitions;

    public RootNode()
    {
        definitions = new ArrayList<DefinitionNode>();
    }

    @Override
    public String toString()
    {
        String output = "";
        
        for (DefinitionNode definitionNode : definitions)
        {
            output += definitionNode;
        }
        
        return output;
    }
}

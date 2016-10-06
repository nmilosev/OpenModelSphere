package com.neosapiens.plugins.reverse.javasource.actions;

public class SemanticActionError extends Error {

    private static final long serialVersionUID = -6557999233726437845L;
    private Exception wrappedException;
    
    public SemanticActionError(Exception wrappedException)
    {
        this.wrappedException = wrappedException;
    }
    
    @Override
    public String getMessage() {
        return wrappedException.getMessage();
    }
    
    public Exception getWrappedException() {
        return wrappedException;
    }
}

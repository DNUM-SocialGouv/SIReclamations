package fr.gouv.social.sireclamations.server_side;

import java.util.Map;

public class GraphQLRequest {

    private String queryId;
    private Map<String, Object> variables;
    private String operationName;

    // Getters et Setters

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}


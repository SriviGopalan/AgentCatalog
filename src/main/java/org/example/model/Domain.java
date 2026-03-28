package org.example.model;

public class Domain {

    private String id;
    private String name;
    private String description;
    private String icon;
    private int agentCount;

    private Domain() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public int getAgentCount() { return agentCount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Domain domain = new Domain();

        public Builder id(String v) { domain.id = v; return this; }
        public Builder name(String v) { domain.name = v; return this; }
        public Builder description(String v) { domain.description = v; return this; }
        public Builder icon(String v) { domain.icon = v; return this; }
        public Builder agentCount(int v) { domain.agentCount = v; return this; }
        public Domain build() { return domain; }
    }
}

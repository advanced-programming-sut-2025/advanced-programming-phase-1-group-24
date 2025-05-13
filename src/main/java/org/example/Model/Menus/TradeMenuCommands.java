package org.example.Model.Menus;

public enum TradeMenuCommands implements Commands{
    OFFER_TRADE("^trade\\s+-u\\s+(?<username>\\S+)\\s+-t\\s+(?<type>.+)\\s+-i\\s+(?<item>.+)\\s+-a\\s+(?<amount>\\d+)\\s+-p\\s+(?<price>\\d+)$"),
    REQUEST_OFFER("^trade\\s+-u\\s+(?<username>\\S+)\\s+-t\\s+(?<type>.+)\\s+-i\\s+(?<item>.+)\\s+-a\\s+(?<amount>\\d+)\\s+-ti\\s+(?<targetItem>.+)\\s+-ta\\s+(?<targetAmount>\\d+)$"),
    TRACE_LIST("^trade\\s+list$"),
    TRADE_RESPONSE("^trade\\s+response\\s+(?<respond>accept|reject)\\s+-i\\s+(?<id>\\d+)$"),
    EXIT_TRADE("^exit\\s+trade"),
    TRADE_HISTORY("^trade\\s+history$");

    private final String regex;
    TradeMenuCommands(String regex) {
        this.regex = regex;
    }
    @Override
    public String getRegex() {
        return regex;
    }
}

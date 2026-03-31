package ru.skillbox.config;

public record Config(String botApiToken) {
    public Config{
        if(botApiToken== null || botApiToken.isEmpty()){
            throw new IllegalArgumentException("Token does not exist");
        }
    }
}

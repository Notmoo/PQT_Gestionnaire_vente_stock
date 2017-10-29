package com.pqt.client.module.network;

import com.pqt.core.entities.server_config.ServerConfig;

import java.util.HashMap;
import java.util.Map;

public class ServerConfigCache {

    private final Map<ServerData, ServerConfig> cache;

    public ServerConfigCache() {
        cache = new HashMap<>();
    }

    public void addServerConfig(String host, Integer port, ServerConfig config){
        ServerData match = cache.keySet().stream().filter(key->key.getHost().equals(host)&&key.getPort().equals(port)).findFirst().orElse(null);
        if(match==null){
            cache.put(new ServerData(host, port), config);
        }else{
            cache.replace(match, config);
        }
    }

    public void removeServerData(String host, Integer port){
        ServerData data = new ServerData(host, port);
        if(cache.containsKey(data))
            cache.remove(data);
    }

    public boolean hasConfig(String host, Integer port){
        return cache.containsKey(new ServerData(host, port));
    }

    public ServerConfig getConfig(String host, Integer port){
        if(hasConfig(host, port))
            return cache.get(new ServerData(host, port));

        return null;
    }

    private class ServerData{
        private String host;
        private Integer port;

        public ServerData(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ServerData that = (ServerData) o;

            if (host != null ? !host.equals(that.host) : that.host != null) return false;
            return port != null ? port.equals(that.port) : that.port == null;
        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + (port != null ? port.hashCode() : 0);
            return result;
        }
    }
}

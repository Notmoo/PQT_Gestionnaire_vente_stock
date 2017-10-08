package com.pqt.client.module.network;

import com.pqt.client.module.network.listeners.INetworkServiceListener;
import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.IMapItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.core.entities.server_config.ConfigFields;
import com.pqt.core.entities.server_config.ServerConfig;

import javax.swing.event.EventListenerList;
import java.util.*;

//TODO ajout javadoc

/*
 * Ce service doit permettre de faire des ping et de récupérer la config d'un serveur distant
 */
public class NetworkService {

    private final QueryExecutor queryExecutor;
    private final EventListenerList listenerList;
    private final ServerConfigCache configCache;

    public NetworkService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
        listenerList = new EventListenerList();
        configCache = new ServerConfigCache();
    }

    public void addListener(INetworkServiceListener l){
        listenerList.add(INetworkServiceListener.class, l);
    }

    public void removeListener(INetworkServiceListener l){
        listenerList.remove(INetworkServiceListener.class, l);
    }

    public void sendPQTPing(String host, Integer port){
        checkData(host, port);
        queryExecutor.executePingQuery(new INoItemMessageCallback() {
            @Override
            public void ack() {
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingSuccessEvent(host, port));
                sendConfigRequest(host, port);
            }

            @Override
            public void err(Throwable cause) {
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingFailureEvent(host, port, cause));
            }

            @Override
            public void ref(Throwable cause) {
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingFailureEvent(host, port, cause));
            }
        });
    }

    public ServerConfig getServerConfig(String host, Integer port){
        checkData(host, port);
        return configCache.getConfig(host, port);
    }

    private void sendConfigRequest(String host, Integer port){
        queryExecutor.executeConfigListQuery(new IMapItemMessageCallback<String, String>(){

            @Override
            public void err(Throwable cause) {
                //TODO ajouter log erreur
            }

            @Override
            public void ref(Throwable cause) {
                //TODO ajouter log erreur
            }

            @Override
            public void ack(Map<String, String> obj) {
                configCache.addServerConfig(host, port, convertToServerConfig(obj));
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(INetworkServiceListener::onNewServerConfigData);
            }
        });
    }

    private ServerConfig convertToServerConfig(Map<String, String> data){

        ServerConfig serverConfig = new ServerConfig();
        List<String> allowedFields = new ArrayList<>();
        EnumSet.allOf(ConfigFields.class).forEach(e->allowedFields.add(e.name()));

        data.keySet()
                .stream()
                .filter(allowedFields::contains)
                .filter(key->isBoolean(data.get(key)))
                .forEach(key->serverConfig.add(getMatchingConfigFields(key), Boolean.parseBoolean(data.get(key))));

        return serverConfig;
    }

    private boolean isBoolean(String str){
        return str.equals("true") || str.equals("false");
    }

    private ConfigFields getMatchingConfigFields(String str){
        ConfigFields match = null;

        EnumSet<ConfigFields> enumSet = EnumSet.allOf(ConfigFields.class);
        for(ConfigFields field : enumSet){
            if(str.equals(field.name()))
                match = field;
        }

        return match;
    }

    private void checkData(String host, Integer port){
        if(host==null || port == null)
            throw new NullPointerException("Null value as server data is not allowed");
        if(host.isEmpty())
            throw new IllegalArgumentException("host cannot be empty");
        if(port<1 || port>65535)
            throw new IllegalArgumentException("port number must be an unsigned 16-bit integer (0<n<65536)");
    }
}

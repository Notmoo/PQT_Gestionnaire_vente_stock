package com.pqt.client.module.network;

import com.pqt.client.module.connection.ConnectionService;
import com.pqt.client.module.network.listeners.INetworkServiceListener;
import com.pqt.client.module.query.QueryExecutor;
import com.pqt.client.module.query.query_callback.IMapItemMessageCallback;
import com.pqt.client.module.query.query_callback.INoItemMessageCallback;
import com.pqt.core.entities.server_config.ConfigFields;
import com.pqt.core.entities.server_config.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.EventListenerList;
import java.util.*;

//TODO Issue #5 : ajout javadoc

/*
 * Ce service doit permettre de faire des ping et de récupérer la config d'un serveur distant
 */
public class NetworkService {

    private static Logger LOGGER = LogManager.getLogger(NetworkService.class);

    private final QueryExecutor queryExecutor;
    private final ConnectionService connectionService;
    private final EventListenerList listenerList;
    private final ServerConfigCache configCache;

    public NetworkService(QueryExecutor queryExecutor, ConnectionService connectionService) {
        LOGGER.info("Initialisation du service 'Network'");
        this.queryExecutor = queryExecutor;
        this.connectionService = connectionService;
        listenerList = new EventListenerList();
        configCache = new ServerConfigCache();
        LOGGER.info("Service 'Network' initialisé");
    }

    public void addListener(INetworkServiceListener l){
        listenerList.add(INetworkServiceListener.class, l);
    }

    public void removeListener(INetworkServiceListener l){
        listenerList.remove(INetworkServiceListener.class, l);
    }

    public void sendPQTPing(String host, Integer port){
        checkData(host, port);
        LOGGER.trace("Envoi d'un ping");
        queryExecutor.executePingQuery(new INoItemMessageCallback() {
            @Override
            public void ack() {
                LOGGER.trace("Réponse au ping -> ACK");
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingSuccessEvent(host, port));
                sendConfigRequest(host, port);
            }

            @Override
            public void err(Throwable cause) {
                LOGGER.trace("Réponse au ping -> ERR");
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingFailureEvent(host, port, cause));
            }

            @Override
            public void ref(Throwable cause) {
                LOGGER.trace("Réponse au ping -> REF");
                Arrays.stream(listenerList.getListeners(INetworkServiceListener.class))
                        .forEach(l->l.onPQTPingFailureEvent(host, port, cause));
            }
        });
    }

    public boolean hasServerConfig(String host, Integer port){
        checkData(host, port);
        return configCache.hasConfig(host, port);
    }

    public ServerConfig getServerConfig(String host, Integer port){
        checkData(host, port);
        return configCache.getConfig(host, port);
    }

    public void setActiveServer(String host, Integer port){
        //TODO changer le nom de context de la webapp
        String webAppContext = "pqt-server";
        connectionService.setServerUrl(String.format("%s:%s/%s", host, port, webAppContext));
    }

    private void sendConfigRequest(String host, Integer port){
        LOGGER.trace("Envoi d'une demande de configuration serveur");
        queryExecutor.executeConfigListQuery(new IMapItemMessageCallback<String, String>(){

            @Override
            public void err(Throwable cause) {
                LOGGER.error("Erreur lors de la demande de configurations serveur : {}", cause);
            }

            @Override
            public void ref(Throwable cause) {
                LOGGER.error("Demande de configurations serveur refusée : {}", cause);
            }

            @Override
            public void ack(Map<String, String> obj) {
                LOGGER.trace("Demande de configuration serveur acceptée");
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

    public void shutdown() {
        LOGGER.info("Fermeture du service 'Network'");
        //Nothing to do
    }
}

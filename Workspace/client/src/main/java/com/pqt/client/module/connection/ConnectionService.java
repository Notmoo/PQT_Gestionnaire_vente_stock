package com.pqt.client.module.connection;

import com.pqt.client.module.connection.listeners.IConnectionListener;
import com.pqt.client.module.connection.senders.HttpTextSender;
import com.pqt.client.module.connection.senders.ITextSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

//TODO Issue #5 : écrire javadoc
public class ConnectionService {

    private static Logger LOGGER = LogManager.getLogger(ConnectionService.class);

    private String serverUrl;
    private ExecutorService executor;
    private ITextSender textSender;

    public ConnectionService(String serverUrl) {
        LOGGER.info("Initialisation du service 'Connection'");
        executor = new ThreadPoolExecutor(1, 1, 1000,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        this.serverUrl = serverUrl;
        this.textSender = new HttpTextSender();
        LOGGER.info("Service 'Connection' initialisé");
    }

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String url){
        LOGGER.info("L'url du serveur est : {}", url);
        this.serverUrl = url;
    }

    /**
     * Arrête le service d'envoi.
     * <p/>
     * <b>Si ce service est arrêté, il ne sera plus possible de l'utiliser, et ne pourra pas être redémarré</b>. Une
     * autre isntance devra être utilisée pour pouvoir recommencer à se servir de ce service.
     * <p/>
     * Si {@code force} vaut true, le service tentera d'interrompre tous les envois de texte en cours, même s'il ne
     * sont pas terminés. Sinon, le service attendra que tous les envois en cours soient terminé avant de s'arrêter, mais
     * n'acceptera pas de nouveaux envois.
     * @param force {@code true} si l'arrêt du service doit être forcé, {@code false} sinon
     */
    public void stop(boolean force) {
        if(executor!=null)
            if(force)
                executor.shutdownNow();
            else
                executor.shutdown();
	}

    /**
     * Envoie la chaîne de caractères {@code text} au serveur de donnée correspondant à l'URL {@link #getServerUrl()}, et
     * utilise {@code listener} pour notifier l'avancement de l'envoi.
     *
     * @param text texte à envoyer
     * @param listener listener à utiliser pour notifier l'avancement de l'envoi (voir {@link IConnectionListener})
     * @throws IllegalStateException Si l'url du serveur vaut {@code null} (à spécifier à la constructyion ou avec
     * {@link #setServerUrl(String)}, ou si le service à été arrêté via la méthode {@link #stop(boolean)}.
     */
	public void sendText(String text, IConnectionListener listener) throws IllegalStateException{
	    if(serverUrl==null)
	        throw new IllegalStateException("No url specified for data server");
	    if(executor.isShutdown() || executor.isTerminated())
	        throw new IllegalStateException("Service was shut down : unable to send text");
        executor.submit(()->textSender.send(serverUrl, text, listener));
    }

    public void shutdown() {
	    LOGGER.info("Fermeture du service 'Connection'");
        stop(false);
    }
}

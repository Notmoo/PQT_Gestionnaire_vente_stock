package com.pqt.server.controller;

import com.pqt.core.entities.messages.Message;

/**
 * Cette interface définit le type général correspondant à un élément censé traiter les objets de la classe {@link Message} arrivant au serveur.
 *
 * @author Guillaume "Cess" Prost
 */
public interface IMessageHandler {
    /**
     * Traite le message {@code message} passé en paramètre. Renvoie un message de réponse en tant que retour de méthode.
     * <p/>
     * Cette méthode doit toujours renvoyer un objet message <b>autre que {@code null}</b>. Un message de type
     * {@link com.pqt.core.entities.messages.MessageType#ERROR_QUERY} ou de type
     * {@link com.pqt.core.entities.messages.MessageType#REFUSED_QUERY} doit-être renvoyé si le message donné ne peut
     * être pris en charge.<br/>
     * Cela signifie aussi que cette méthode <b>ne doit pas lever d'exception</b>, et que ces dernières doivent être
     * gérées en interne.
     * <p/>
     * Pour plus de détail sur les messages, leurs significations et les réponses attendues, voir la documentation du
     * projet.
     * @param message Objet de la classe {@link Message} à traiter.
     *
     * @return Objet de la classe {@link Message} correspondant à la réponse au paramètre {@code message}.
     */
    Message handleMessage(Message message);
}

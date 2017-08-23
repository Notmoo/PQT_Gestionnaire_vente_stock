package com.pqt.client.gui.ressources.strings;

public interface IObjectWithQuantityStringRenderer<T> {
    String render(T obj, int quantity);
}

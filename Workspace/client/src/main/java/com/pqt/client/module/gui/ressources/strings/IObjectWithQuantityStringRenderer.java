package com.pqt.client.module.gui.ressources.strings;

public interface IObjectWithQuantityStringRenderer<T> {
    String render(T obj, int quantity);
}

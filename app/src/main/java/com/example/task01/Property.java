package com.example.task01;

import java.util.ArrayList;

public class Property<T> {
    private T value;
    private Object owner;
    private T oldValue;
    private ArrayList<PropertyListener> listeners = new ArrayList<>();

    public Property(Object owner, T initialValue){
        this.value = initialValue;
        this.owner = owner;
    }
    public Object getOwner(){
        return owner;
    }
    public T get(){
        return value;
    }
    public void set(T newValue){
        oldValue = value;
        this.value = newValue;
        notifyListeners();
    }
    public void addListener(PropertyListener<T> listener){
        listeners.add(listener);
    }
    public void removeListener(PropertyListener<T> listener){
        listeners.remove(listener);
    }
    protected void notifyListeners(){
        for(int i=0;i<listeners.size();i++){
            listeners.get(i).valueChanged(this,oldValue,value);
        }
    }
}

package org.hinoob.generator;


import org.bukkit.Location;

public class Generator {

    public Location location;
    public GeneratorType generatorType;

    public Generator(Location location, GeneratorType type){
        this.location = location;
        this.generatorType = type;
    }
}

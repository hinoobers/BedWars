package org.hinoob.setup;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.hinoob.generator.Generator;
import org.hinoob.island.Island;

import java.util.ArrayList;
import java.util.List;

public class SetupData {

    public boolean active = false;

    public Inventory backupInventory;

    public String gameName;
    public int colorIndex = 0;

    public Location spawnLocation;

    public Location editingIslandSpawnLocation;
    public Location editingIslandResourceLocation;
    public String editingIslandColor;
    public Location editingIslandPersonalShopLocation;
    public Location editingIslandTeamShopLocation;
    public Location editingBedLocation;

    public List<Generator> generators = new ArrayList<>();

    public List<Island> islands = new ArrayList<>();
}

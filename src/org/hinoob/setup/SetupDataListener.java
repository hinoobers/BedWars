package org.hinoob.setup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.hinoob.BedWars;
import org.hinoob.generator.Generator;
import org.hinoob.generator.GeneratorType;
import org.hinoob.island.Island;

import java.util.Arrays;
import java.util.Set;

public class SetupDataListener implements Listener {

    public enum Color {
        WHITE_WOOL,
        YELLOW_WOOL,
        GREEN_WOOl,
        RED_WOOL,
        BLUE_WOOL,
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        SetupDataManager.addData(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        SetupData setupData = SetupDataManager.getData(e.getPlayer());
        if(setupData != null && setupData.active){
            if(e.getItem() == null) return;
            if(!e.getItem().hasItemMeta()) return;

            String displayName = ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName());
            if(e.getItem().getItemMeta().hasLore() && e.getItem().getItemMeta().getLore().stream().anyMatch(b -> b.equalsIgnoreCase("color"))) {


                if (setupData.colorIndex >= Color.values().length - 1) {
                    setupData.colorIndex = 0;
                } else {
                    ++setupData.colorIndex;
                }

                Color newColor = Color.values()[setupData.colorIndex];
                Material material = Arrays.stream(Material.values()).filter(p -> p.name().equalsIgnoreCase(newColor.name())).findAny().get();

                ItemMeta itemMeta = e.getItem().getItemMeta();
                String colorPart = newColor.name().substring(0, newColor.name().indexOf('_'));
                String startCharacter = colorPart.charAt(0) + "";
                itemMeta.setDisplayName(ChatColor.valueOf(colorPart.toUpperCase()) + startCharacter.toUpperCase() + colorPart.toLowerCase().substring(1));

                setupData.editingIslandColor = colorPart.toUpperCase();
                e.getItem().setItemMeta(itemMeta);
                e.getItem().setType(material);
            }else if(displayName.equalsIgnoreCase( "Set island spawn location")) {
                setupData.editingIslandSpawnLocation = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
            }else if(displayName.equalsIgnoreCase("Set spawn location")){
                setupData.spawnLocation = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
            }else if(displayName.equalsIgnoreCase("Set island resource spawn location")) {
                setupData.editingIslandResourceLocation = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
            }else if(displayName.equalsIgnoreCase("Set island personal shop location")) {
                setupData.editingIslandPersonalShopLocation = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
            }else if(displayName.equalsIgnoreCase("Set island team shop location")) {
                setupData.editingIslandTeamShopLocation = e.getPlayer().getLocation();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
            }else if(displayName.equalsIgnoreCase("Set island bed location")){
                Block block = e.getPlayer().getTargetBlock((Set<Material>)null, 6);

                if(block != null && block.getType().toString().toLowerCase().contains("bed") && block.getType() != Material.BEDROCK){
                    setupData.editingBedLocation = block.getLocation();
                    e.getPlayer().sendMessage(ChatColor.GREEN + "Set!");
                }else{
                    e.getPlayer().sendMessage(ChatColor.RED + "You must be looking at a bed!");
                }
            }else if(displayName.equalsIgnoreCase("Add diamond generator")) {
                Generator generator = new Generator(e.getPlayer().getLocation(), GeneratorType.DIAMOND, null);
                setupData.generators.add(generator);
                e.getPlayer().sendMessage(ChatColor.GREEN + "Added diamond generator!");
            }else if(displayName.equalsIgnoreCase("Add emerald generator")){
                Generator generator = new Generator(e.getPlayer().getLocation(), GeneratorType.EMERALD, null);
                setupData.generators.add(generator);
                e.getPlayer().sendMessage(ChatColor.GREEN + "Added emerald generator!");
            }else if(displayName.equalsIgnoreCase("Add Island")){
                if(setupData.editingIslandColor != null &&
                        setupData.editingIslandTeamShopLocation != null &&
                        setupData.editingIslandPersonalShopLocation != null &&
                        setupData.editingIslandSpawnLocation != null &&
                        setupData.editingIslandResourceLocation != null &&
                        setupData.editingBedLocation != null
                ){
                    Island island = new Island();
                    island.setTeamShopLocation(setupData.editingIslandTeamShopLocation);
                    island.setPersonalShopLocation(setupData.editingIslandPersonalShopLocation);
                    island.setSpawnLocation(setupData.editingIslandSpawnLocation);
                    island.setResourceSpawnLocation(setupData.editingIslandResourceLocation);
                    island.setColor(setupData.editingIslandColor);
                    island.setBedLocation(setupData.editingBedLocation);
                    setupData.islands.add(island);
                    e.getPlayer().sendMessage(ChatColor.GREEN + "New island added! (Now you have " + setupData.islands.size() + " island(s) setup)");
                }else{
                    e.getPlayer().sendMessage(ChatColor.RED + "You must set all the values for the island!");
                }
            }else if(displayName.equalsIgnoreCase("Finish")){
                if(setupData.islands.size() <= 1){
                    e.getPlayer().sendMessage(ChatColor.RED + "You must have atleast 2 islands.");
                }else{
                    if(setupData.spawnLocation == null){
                        e.getPlayer().sendMessage(ChatColor.RED + "You must set the spawn location.");
                    }else{
                        BedWars bedWars = BedWars.INSTANCE;
                        bedWars.getConfig().set("games." + setupData.gameName + ".world", setupData.spawnLocation.getWorld().getName());

                        bedWars.getConfig().set("games." + setupData.gameName + ".spawn-location.x", setupData.spawnLocation.getX());
                        bedWars.getConfig().set("games." + setupData.gameName + ".spawn-location.y", setupData.spawnLocation.getY());
                        bedWars.getConfig().set("games." + setupData.gameName + ".spawn-location.z", setupData.spawnLocation.getZ());
                        bedWars.getConfig().set("games." + setupData.gameName + ".spawn-location.yaw", setupData.spawnLocation.getYaw());
                        bedWars.getConfig().set("games." + setupData.gameName + ".spawn-location.pitch", setupData.spawnLocation.getPitch());


                        for(Island island : setupData.islands){
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".spawn-location.x", island.getSpawnLocation().getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".spawn-location.y", island.getSpawnLocation().getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".spawn-location.z", island.getSpawnLocation().getZ());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".spawn-location.yaw", island.getSpawnLocation().getYaw());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".spawn-location.pitch", island.getSpawnLocation().getPitch());


                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".bed-location.x", island.getBedLocation().getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".bed-location.y", island.getBedLocation().getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".bed-location.z", island.getBedLocation().getZ());

                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".resource-spawn-location.x", island.getResourceSpawnLocation().getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".resource-spawn-location.y", island.getResourceSpawnLocation().getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".resource-spawn-location.z", island.getResourceSpawnLocation().getZ());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".resource-spawn-location.yaw", island.getResourceSpawnLocation().getYaw());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".resource-spawn-location.pitch", island.getResourceSpawnLocation().getPitch());

                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".team-shop-location.x", island.getTeamShopLocation().getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".team-shop-location.y", island.getTeamShopLocation().getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".team-shop-location.z", island.getTeamShopLocation().getZ());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".team-shop-location.yaw", island.getTeamShopLocation().getYaw());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".team-shop-location.pitch", island.getTeamShopLocation().getPitch());

                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".personal-shop-location.x", island.getPersonalShopLocation().getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".personal-shop-location.y", island.getPersonalShopLocation().getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".personal-shop-location.z", island.getPersonalShopLocation().getZ());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".personal-shop-location.yaw", island.getPersonalShopLocation().getYaw());
                            bedWars.getConfig().set("games." + setupData.gameName + ".islands." + island.getColor() + ".personal-shop-location.pitch", island.getPersonalShopLocation().getPitch());

                        }

                        int index = 0;
                        for(Generator generator : setupData.generators){
                            bedWars.getConfig().set("games." + setupData.gameName + ".generators.gen" + index + ".type", generator.generatorType.name());
                            bedWars.getConfig().set("games." + setupData.gameName + ".generators.gen" + index + ".location.x", generator.location.getX());
                            bedWars.getConfig().set("games." + setupData.gameName + ".generators.gen" + index + ".location.y", generator.location.getY());
                            bedWars.getConfig().set("games." + setupData.gameName + ".generators.gen" + index + ".location.z", generator.location.getZ());
                            ++index;
                        }

                        bedWars.saveConfig();

                    }
                }
            }
        }
    }
}
